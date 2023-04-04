package br.com.uboard.model.transport;

import java.io.Serializable;
import java.util.List;

public class SyncProjectDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long userUUID;
	private List<ProjectDTO> projects;

	public SyncProjectDTO() {

	}

	public SyncProjectDTO(Long userUUID, List<ProjectDTO> projects) {
		this.userUUID = userUUID;
		this.projects = projects;
	}

	public Long getUserUUID() {
		return userUUID;
	}

	public void setUserUUID(Long userUUID) {
		this.userUUID = userUUID;
	}

	public List<ProjectDTO> getProjects() {
		return projects;
	}

	public void setProjects(List<ProjectDTO> projects) {
		this.projects = projects;
	}

}
