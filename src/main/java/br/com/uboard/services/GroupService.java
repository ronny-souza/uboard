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

import br.com.uboard.exceptions.GroupNotFoundException;
import br.com.uboard.exceptions.SynchronizeGroupsException;
import br.com.uboard.exceptions.UserNotFoundException;
import br.com.uboard.model.Grouping;
import br.com.uboard.model.User;
import br.com.uboard.model.enums.GitlabAPIEnum;
import br.com.uboard.model.transport.GroupDTO;
import br.com.uboard.model.transport.SyncGroupDTO;
import br.com.uboard.model.transport.SyncMilestoneDTO;
import br.com.uboard.model.transport.UserDTO;
import br.com.uboard.rabbitmq.MessageService;
import br.com.uboard.rabbitmq.RabbitQueues;
import br.com.uboard.repository.GroupingRepository;

@Service
public class GroupService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);

	private GroupingRepository groupingRepository;
	private UserService userService;
	private MessageService messageService;

	private WebClientRest webClient;

	@Value("${gitlab-provider}")
	private String address;

	public GroupService(GroupingRepository groupingRepository, MessageService messageService, UserService userService) {
		this.groupingRepository = groupingRepository;
		this.userService = userService;
		this.messageService = messageService;
		this.webClient = new WebClientRest();
	}

	public Grouping findByGitlabIdentifier(Long gitlabIdentifier) throws GroupNotFoundException {
		Optional<Grouping> optionalGroup = this.groupingRepository.findByGitlabIdentifier(gitlabIdentifier);

		if (optionalGroup.isEmpty()) {
			throw new GroupNotFoundException(String.format("Group %d not found", gitlabIdentifier));
		}

		return optionalGroup.get();
	}

	public List<GroupDTO> fetchGroupsToSync(UserDTO userInSession)
			throws SynchronizeGroupsException, UserNotFoundException {

		User user = this.userService.findByUboardIdentifier(userInSession.getUboardIdentifier());

		List<GroupDTO> groups = new ArrayList<>();
		List<GroupDTO> newGroups = this.fetchGroups(userInSession);
		List<Grouping> currentGroups = this.groupingRepository.findByUser(user);

		for (GroupDTO newGroup : newGroups) {
			Optional<Grouping> optionalGroup = currentGroups.stream()
					.filter(currentGroup -> currentGroup.getGitlabIdentifier().equals(newGroup.getId())).findAny();

			if (optionalGroup.isEmpty()) {
				groups.add(newGroup);
			}
		}

		return groups;
	}

	public void saveGroups(List<GroupDTO> groups, UserDTO userInSession) throws UserNotFoundException {
		if (groups == null || groups.isEmpty()) {
			return;
		}

		User user = this.userService.findByUboardIdentifier(userInSession.getUboardIdentifier());

		for (GroupDTO newGroupDTO : groups) {
			Grouping grouping = new Grouping(newGroupDTO, user);
			this.groupingRepository.save(grouping);

			SyncMilestoneDTO syncMilestoneDTO = new SyncMilestoneDTO(user.getGitlabIdentifier(), newGroupDTO.getId());
			this.messageService.enqueue(RabbitQueues.GITLAB_REQUEST_SYNC_GROUP_MILESTONES, syncMilestoneDTO);
		}
	}

	private List<GroupDTO> fetchGroups(UserDTO userInSession) throws SynchronizeGroupsException {
		HttpEntity<Object> httpEntity = this.webClient.getDefaultHttpEntity(userInSession.getId());
		String url = new StringBuilder().append(this.address).append(GitlabAPIEnum.GROUP.getPath()).append("/sync")
				.toString();

		ResponseEntity<List<GroupDTO>> response = this.webClient.get(url, httpEntity,
				new ParameterizedTypeReference<List<GroupDTO>>() {
				});

		if (response.getStatusCode() != HttpStatus.OK) {
			throw new SynchronizeGroupsException("Error on sync Gitlab Groups");
		}

		return response.getBody();
	}

	public void synchronizeGroups(SyncGroupDTO syncGroupDTO) {
		try {
			if (syncGroupDTO.getUserUUID() == null) {
				throw new IllegalArgumentException("User identifier is not found");
			}

			if (syncGroupDTO.getGroups() == null || syncGroupDTO.getGroups().isEmpty()) {
				return;
			}

			User user = this.userService.findByGitlabIdentifier(syncGroupDTO.getUserUUID());

			List<Grouping> currentGroups = this.groupingRepository.findByUser(user);

			for (GroupDTO newGroupDTO : syncGroupDTO.getGroups()) {
				Optional<Grouping> groupExists = currentGroups.stream()
						.filter(currentGroup -> currentGroup.getGitlabIdentifier().equals(newGroupDTO.getId()))
						.findAny();

				if (groupExists.isPresent()) {
					Grouping currentGroup = groupExists.get();
					currentGroup.setName(newGroupDTO.getName());
					currentGroup.setAvatarUrl(newGroupDTO.getAvatarUrl() != null ? newGroupDTO.getAvatarUrl()
							: currentGroup.getAvatarUrl());
					this.groupingRepository.save(currentGroup);
					currentGroups.removeIf(group -> group.getGitlabIdentifier().equals(newGroupDTO.getId()));
				} else {
					Grouping newGroup = new Grouping(newGroupDTO, user);
					this.groupingRepository.save(newGroup);
				}
			}

			currentGroups.stream().forEach(groupToDelete -> this.groupingRepository.delete(groupToDelete));

		} catch (UserNotFoundException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
