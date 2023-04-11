package br.com.uboard.model.transport;

import java.io.Serializable;

import br.com.uboard.model.Grouping;

public class GroupDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String uboardIdentifier;
	private boolean autoSync;
	private String name;
	private String avatarUrl;

	public GroupDTO() {

	}

	public GroupDTO(Grouping group) {
		this.id = group.getGitlabIdentifier();
		this.uboardIdentifier = group.getUboardIdentifier();
		this.autoSync = group.isAutoSync();
		this.name = group.getName();
		this.avatarUrl = group.getAvatarUrl();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUboardIdentifier() {
		return uboardIdentifier;
	}

	public void setUboardIdentifier(String uboardIdentifier) {
		this.uboardIdentifier = uboardIdentifier;
	}

	public boolean isAutoSync() {
		return autoSync;
	}

	public void setAutoSync(boolean autoSync) {
		this.autoSync = autoSync;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

}
