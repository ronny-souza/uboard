package br.com.uboard.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.uboard.exceptions.SynchronizeGroupsException;
import br.com.uboard.exceptions.UserNotFoundException;
import br.com.uboard.model.transport.GroupDTO;
import br.com.uboard.model.transport.UserDTO;
import br.com.uboard.services.GroupService;
import br.com.uboard.services.UserSessionService;

@RestController
@RequestMapping("/group")
public class GroupController {

	private GroupService groupService;
	private UserSessionService userSessionService;

	public GroupController(GroupService groupService, UserSessionService userSessionService) {
		this.groupService = groupService;
		this.userSessionService = userSessionService;
	}

	@GetMapping("/sync")
	public ResponseEntity<List<GroupDTO>> fetchGroupsToSync() throws SynchronizeGroupsException, UserNotFoundException {
		UserDTO userInSession = this.userSessionService.getUserInSession();
		List<GroupDTO> response = this.groupService.fetchGroupsToSync(userInSession);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/sync")
	public ResponseEntity<Void> synchronizeGroups(@RequestBody List<GroupDTO> groupsToSync)
			throws UserNotFoundException {
		UserDTO userInSession = this.userSessionService.getUserInSession();
		this.groupService.saveGroups(groupsToSync, userInSession);
		return ResponseEntity.ok().build();
	}

}
