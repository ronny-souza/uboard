package br.com.uboard.model;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.uboard.model.transport.GroupDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Grouping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "uboardIdentifier", nullable = false, unique = true)
	private String uboardIdentifier;

	@Column(name = "gitlabIdentifier", nullable = false, unique = true)
	private Long gitlabIdentifier;

	@Column(name = "autoSync", columnDefinition = "tinyint DEFAULT 0")
	private boolean autoSync;

	@Column(nullable = false)
	private LocalDateTime lastSync;

	@Column(nullable = false)
	private String name;

	@Column(name = "avatarUrl", nullable = true)
	private String avatarUrl;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	private User user;

	public Grouping() {

	}

	public Grouping(GroupDTO groupDTO, User user) {
		this.uboardIdentifier = UUID.randomUUID().toString();
		this.gitlabIdentifier = groupDTO.getId();
		this.autoSync = false;
		this.lastSync = LocalDateTime.now();
		this.name = groupDTO.getName();
		this.avatarUrl = groupDTO.getAvatarUrl();
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

	public boolean isAutoSync() {
		return autoSync;
	}

	public void setAutoSync(boolean autoSync) {
		this.autoSync = autoSync;
	}

	public LocalDateTime getLastSync() {
		return lastSync;
	}

	public void setLastSync(LocalDateTime lastSync) {
		this.lastSync = lastSync;
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
