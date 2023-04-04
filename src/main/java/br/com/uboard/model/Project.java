package br.com.uboard.model;

import java.util.UUID;

import br.com.uboard.model.transport.ProjectDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "uboardIdentifier", nullable = false, unique = true)
	private String uboardIdentifier;

	@Column(name = "gitlabIdentifier", nullable = false, unique = true)
	private Long gitlabIdentifier;

	@Column(nullable = false)
	private String name;

	@Column(name = "avatarUrl", nullable = true)
	private String avatarUrl;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	private User user;

	public Project() {

	}

	public Project(ProjectDTO projectDTO, User user) {
		this.uboardIdentifier = UUID.randomUUID().toString();
		this.gitlabIdentifier = projectDTO.getId();
		this.name = projectDTO.getName();
		this.avatarUrl = projectDTO.getAvatarUrl();
		this.user = user;
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

	public Long getGitlabIdentifier() {
		return gitlabIdentifier;
	}

	public void setGitlabIdentifier(Long gitlabIdentifier) {
		this.gitlabIdentifier = gitlabIdentifier;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
