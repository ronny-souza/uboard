package br.com.uboard.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import br.com.uboard.model.transport.MilestoneDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Milestone {

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
	private String name;

	@Column(nullable = true)
	private String description;

	@Column(nullable = false)
	private LocalDate startDate;

	@Column(nullable = true)
	private LocalDate dueDate;

	@Column(nullable = true)
	private LocalDateTime lastSync;

	private String state;

	@ManyToOne
	@JoinColumn(name = "group_id", referencedColumnName = "id", nullable = true)
	private Grouping group;

	@ManyToOne
	@JoinColumn(name = "project_id", referencedColumnName = "id", nullable = true)
	private Project project;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	private User user;

	public Milestone() {

	}

	public Milestone(MilestoneDTO milestoneDTO, User user) {
		this.uboardIdentifier = UUID.randomUUID().toString();
		this.gitlabIdentifier = milestoneDTO.getId();
		this.autoSync = false;
		this.name = milestoneDTO.getTitle();
		this.description = milestoneDTO.getDescription();
		this.lastSync = LocalDateTime.now();
		this.state = milestoneDTO.getState();
		this.user = user;

		if (milestoneDTO.getStartDate() != null) {
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			this.startDate = LocalDate.parse(milestoneDTO.getStartDate(), dateTimeFormatter);

			if (milestoneDTO.getDueDate() != null) {
				this.dueDate = LocalDate.parse(milestoneDTO.getDueDate(), dateTimeFormatter);
			}
		}
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public LocalDateTime getLastSync() {
		return lastSync;
	}

	public void setLastSync(LocalDateTime lastSync) {
		this.lastSync = lastSync;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Grouping getGroup() {
		return group;
	}

	public void setGroup(Grouping group) {
		this.group = group;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
