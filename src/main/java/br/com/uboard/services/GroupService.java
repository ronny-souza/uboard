package br.com.uboard.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.uboard.exceptions.UserNotFoundException;
import br.com.uboard.model.Grouping;
import br.com.uboard.model.User;
import br.com.uboard.model.transport.GroupDTO;
import br.com.uboard.model.transport.SyncGroupDTO;
import br.com.uboard.repository.GroupingRepository;

@Service
public class GroupService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);

	private GroupingRepository groupingRepository;
	private UserService userService;

	public GroupService(GroupingRepository groupingRepository, UserService userService) {
		this.groupingRepository = groupingRepository;
		this.userService = userService;
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
