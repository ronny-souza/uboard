package br.com.uboard.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.uboard.exceptions.ProjectNotFoundException;
import br.com.uboard.exceptions.SynchronizeProjectsException;
import br.com.uboard.exceptions.UserNotFoundException;
import br.com.uboard.model.Project;
import br.com.uboard.model.User;
import br.com.uboard.model.enums.GitlabAPIEnum;
import br.com.uboard.model.transport.ProjectDTO;
import br.com.uboard.model.transport.SyncMilestoneDTO;
import br.com.uboard.model.transport.SyncProjectDTO;
import br.com.uboard.model.transport.UserDTO;
import br.com.uboard.rabbitmq.MessageService;
import br.com.uboard.rabbitmq.RabbitQueues;
import br.com.uboard.repository.ProjectRepository;

@Service
public class ProjectService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectService.class);

	private ProjectRepository projectRepository;
	private UserService userService;
	private MessageService messageService;

	private WebClientRest webClient;

	@Value("${gitlab-provider}")
	private String address;

	public ProjectService(ProjectRepository projectRepository, UserService userService, MessageService messageService) {
		this.projectRepository = projectRepository;
		this.userService = userService;
		this.messageService = messageService;
		this.webClient = new WebClientRest();
	}

	public Project findByGitlabIdentifier(Long gitlabIdentifier) throws ProjectNotFoundException {
		Optional<Project> optionalProject = this.projectRepository.findByGitlabIdentifier(gitlabIdentifier);

		if (optionalProject.isEmpty()) {
			throw new ProjectNotFoundException(String.format("Project %d not found", gitlabIdentifier));
		}

		return optionalProject.get();
	}

	public List<ProjectDTO> fetchProjectsToSync(UserDTO userInSession)
			throws SynchronizeProjectsException, UserNotFoundException {

		User user = this.userService.findByUboardIdentifier(userInSession.getUboardIdentifier());

		List<ProjectDTO> projects = new ArrayList<>();
		List<ProjectDTO> newProjects = this.fetchProjects(userInSession);
		List<Project> currentProjects = this.projectRepository.findByUser(user);

		for (ProjectDTO newProject : newProjects) {
			Optional<Project> optionalProject = currentProjects.stream()
					.filter(currentProject -> currentProject.getGitlabIdentifier().equals(newProject.getId()))
					.findAny();

			if (optionalProject.isEmpty()) {
				projects.add(newProject);
			}
		}

		return projects;
	}

	public void saveProjects(List<ProjectDTO> projects, UserDTO userInSession) throws UserNotFoundException {
		if (projects == null || projects.isEmpty()) {
			return;
		}

		User user = this.userService.findByUboardIdentifier(userInSession.getUboardIdentifier());
		List<Project> newProjects = projects.stream().map(projectDTO -> new Project(projectDTO, user)).toList();

		for (Project newProject : newProjects) {
			this.projectRepository.save(newProject);

			SyncMilestoneDTO syncMilestoneDTO = new SyncMilestoneDTO(user.getGitlabIdentifier(),
					newProject.getGitlabIdentifier());
			this.messageService.enqueue(RabbitQueues.GITLAB_REQUEST_SYNC_PROJECT_MILESTONES, syncMilestoneDTO);
		}
	}

	private List<ProjectDTO> fetchProjects(UserDTO userInSession) throws SynchronizeProjectsException {
		HttpEntity<Object> httpEntity = this.webClient.getDefaultHttpEntity(userInSession.getId());
		String url = new StringBuilder().append(this.address).append(GitlabAPIEnum.PROJECT.getPath()).append("/sync")
				.toString();

		ResponseEntity<List<ProjectDTO>> response = this.webClient.get(url, httpEntity,
				new ParameterizedTypeReference<List<ProjectDTO>>() {
				});

		if (response.getStatusCode() != HttpStatus.OK) {
			throw new SynchronizeProjectsException("Error on sync Gitlab Projects");
		}

		return response.getBody();
	}

//	TODO Unused, for now
	public void synchronizeProjects(SyncProjectDTO syncProjectDTO) {
		try {
			if (syncProjectDTO.getUserUUID() == null) {
				throw new IllegalArgumentException("User identifier is not found");
			}

			if (syncProjectDTO.getProjects() == null || syncProjectDTO.getProjects().isEmpty()) {
				return;
			}

			User user = this.userService.findByGitlabIdentifier(syncProjectDTO.getUserUUID());

			List<Project> currentProjects = this.projectRepository.findByUser(user);

			for (ProjectDTO newProjectDTO : syncProjectDTO.getProjects()) {
				Optional<Project> projectExists = currentProjects.stream()
						.filter(currentProject -> currentProject.getGitlabIdentifier().equals(newProjectDTO.getId()))
						.findAny();

				if (projectExists.isPresent()) {
					Project currentProject = projectExists.get();
					currentProject.setName(newProjectDTO.getName());
					currentProject.setAvatarUrl(newProjectDTO.getAvatarUrl() != null ? newProjectDTO.getAvatarUrl()
							: currentProject.getAvatarUrl());
					this.projectRepository.save(currentProject);
					currentProjects.removeIf(project -> project.getGitlabIdentifier().equals(newProjectDTO.getId()));
				} else {
					Project newProject = new Project(newProjectDTO, user);
					this.projectRepository.save(newProject);
				}
			}

			currentProjects.stream().forEach(projectToDelete -> this.projectRepository.delete(projectToDelete));

		} catch (UserNotFoundException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}
