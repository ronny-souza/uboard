package br.com.uboard.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.uboard.exceptions.GroupNotFoundException;
import br.com.uboard.exceptions.ProjectNotFoundException;
import br.com.uboard.exceptions.UserNotFoundException;
import br.com.uboard.model.Grouping;
import br.com.uboard.model.Milestone;
import br.com.uboard.model.Project;
import br.com.uboard.model.User;
import br.com.uboard.model.transport.MilestoneDTO;
import br.com.uboard.model.transport.SyncMilestoneDTO;
import br.com.uboard.repository.MilestoneRepository;

@Service
public class MilestoneService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MilestoneService.class);

	private UserService userService;
	private GroupService groupService;
	private ProjectService projectService;

	private MilestoneRepository milestoneRepository;

	@Value("${gitlab-provider}")
	private String address;

	public MilestoneService(UserService userService, GroupService groupService, ProjectService projectService,
			MilestoneRepository milestoneRepository) {
		this.userService = userService;
		this.groupService = groupService;
		this.projectService = projectService;
		this.milestoneRepository = milestoneRepository;
	}

	public void synchronizeGroupMilestones(SyncMilestoneDTO syncMilestoneDTO)
			throws GroupNotFoundException, UserNotFoundException {
		if (syncMilestoneDTO.getUserUUID() == null) {
			throw new IllegalArgumentException("User identifier is not found");
		}

		if (syncMilestoneDTO.getMilestones() == null || syncMilestoneDTO.getMilestones().isEmpty()) {
			return;
		}

		User user = this.userService.findByGitlabIdentifier(syncMilestoneDTO.getUserUUID());
		Grouping group = this.groupService.findByGitlabIdentifier(syncMilestoneDTO.getContextID());

		List<Milestone> currentMilestones = this.milestoneRepository.findByUserAndGroup(user, group);

		for (MilestoneDTO newMilestoneDTO : syncMilestoneDTO.getMilestones()) {
			Optional<Milestone> milestoneExists = currentMilestones.stream()
					.filter(currentMilestone -> currentMilestone.getGitlabIdentifier().equals(newMilestoneDTO.getId()))
					.findAny();

			if (milestoneExists.isPresent()) {
				Milestone currentMilestone = milestoneExists.get();
				currentMilestone.setName(newMilestoneDTO.getTitle());
				currentMilestone.setLastSync(LocalDateTime.now());
				this.milestoneRepository.save(currentMilestone);
				currentMilestones
						.removeIf(milestone -> milestone.getGitlabIdentifier().equals(newMilestoneDTO.getId()));
			} else {
				Milestone newMilestone = new Milestone(newMilestoneDTO, user);
				newMilestone.setGroup(group);
				this.milestoneRepository.save(newMilestone);
			}
		}

		currentMilestones.stream().forEach(milestoneToDelete -> this.milestoneRepository.delete(milestoneToDelete));
		LOGGER.info(String.format("Milestones sync from group %s successfully", group.getName()));
	}

	public void synchronizeProjectMilestones(SyncMilestoneDTO syncMilestoneDTO)
			throws UserNotFoundException, ProjectNotFoundException {
		if (syncMilestoneDTO.getUserUUID() == null) {
			throw new IllegalArgumentException("User identifier is not found");
		}

		if (syncMilestoneDTO.getMilestones() == null || syncMilestoneDTO.getMilestones().isEmpty()) {
			return;
		}

		User user = this.userService.findByGitlabIdentifier(syncMilestoneDTO.getUserUUID());
		Project project = this.projectService.findByGitlabIdentifier(syncMilestoneDTO.getContextID());

		List<Milestone> currentMilestones = this.milestoneRepository.findByUserAndProject(user, project);

		for (MilestoneDTO newMilestoneDTO : syncMilestoneDTO.getMilestones()) {
			Optional<Milestone> milestoneExists = currentMilestones.stream()
					.filter(currentMilestone -> currentMilestone.getGitlabIdentifier().equals(newMilestoneDTO.getId()))
					.findAny();

			if (milestoneExists.isPresent()) {
				Milestone currentMilestone = milestoneExists.get();
				currentMilestone.setName(newMilestoneDTO.getTitle());
				currentMilestone.setLastSync(LocalDateTime.now());
				this.milestoneRepository.save(currentMilestone);
				currentMilestones
						.removeIf(milestone -> milestone.getGitlabIdentifier().equals(newMilestoneDTO.getId()));
			} else {
				Milestone newMilestone = new Milestone(newMilestoneDTO, user);
				newMilestone.setProject(project);
				this.milestoneRepository.save(newMilestone);
			}
		}

		currentMilestones.stream().forEach(milestoneToDelete -> this.milestoneRepository.delete(milestoneToDelete));
		LOGGER.info(String.format("Milestones sync from project %s successfully", project.getName()));
	}

}
