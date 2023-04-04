package br.com.uboard.model.transport;

import java.io.Serializable;
import java.util.List;

public class SyncGroupDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long userUUID;
	private List<GroupDTO> groups;

	public SyncGroupDTO() {

	}

	public SyncGroupDTO(Long userUUID, List<GroupDTO> groups) {
		this.userUUID = userUUID;
		this.groups = groups;
	}

	public Long getUserUUID() {
		return userUUID;
	}

	public void setUserUUID(Long userUUID) {
		this.userUUID = userUUID;
	}

	public List<GroupDTO> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupDTO> groups) {
		this.groups = groups;
	}

}
