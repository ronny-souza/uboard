package br.com.uboard.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.uboard.exceptions.UserNotFoundException;
import br.com.uboard.model.Project;
import br.com.uboard.model.User;
import br.com.uboard.model.transport.ProjectDTO;
import br.com.uboard.model.transport.SyncProjectDTO;
import br.com.uboard.repository.ProjectRepository;

@Service
public class ProjectService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectService.class);

	private ProjectRepository projectRepository;
	private UserService userService;

	public ProjectService(ProjectRepository projectRepository, UserService userService) {
		this.projectRepository = projectRepository;
		this.userService = userService;
	}

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
