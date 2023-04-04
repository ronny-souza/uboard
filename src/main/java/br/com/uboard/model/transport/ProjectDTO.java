package br.com.uboard.model.transport;

import java.io.Serializable;

import br.com.uboard.model.Project;

public class ProjectDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String uboardIdentifier;
	private String name;
	private String avatarUrl;

	public ProjectDTO() {

	}

	public ProjectDTO(Project project) {
		this.uboardIdentifier = project.getUboardIdentifier();
		this.id = project.getGitlabIdentifier();
		this.name = project.getName();
		this.avatarUrl = project.getAvatarUrl();
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
