package br.com.uboard.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.uboard.exceptions.SynchronizeProjectsException;
import br.com.uboard.exceptions.UserNotFoundException;
import br.com.uboard.model.transport.ProjectDTO;
import br.com.uboard.model.transport.UserDTO;
import br.com.uboard.services.ProjectService;
import br.com.uboard.services.UserSessionService;

@RestController
@RequestMapping("/project")
public class ProjectController {

	private ProjectService projectService;
	private UserSessionService userSessionService;

	public ProjectController(ProjectService projectService, UserSessionService userSessionService) {
		this.projectService = projectService;
		this.userSessionService = userSessionService;
	}

	@GetMapping("/sync")
	public ResponseEntity<List<ProjectDTO>> fetchProjectsToSync()
			throws SynchronizeProjectsException, UserNotFoundException {
		UserDTO userInSession = this.userSessionService.getUserInSession();
		List<ProjectDTO> response = this.projectService.fetchProjectsToSync(userInSession);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/sync")
	public ResponseEntity<Void> synchronizeProjects(@RequestBody List<ProjectDTO> projectsToSync)
			throws UserNotFoundException {
		UserDTO userInSession = this.userSessionService.getUserInSession();
		this.projectService.saveProjects(projectsToSync, userInSession);
		return ResponseEntity.ok().build();
	}

}
