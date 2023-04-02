package br.com.uboard.model.transport;

import java.io.Serializable;

import br.com.uboard.model.User;
import jakarta.validation.constraints.NotBlank;

public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String uboardIdentifier;

	/** Gitlab user identifier */
	private Long id;

	@NotBlank(message = "Name must be provided")
	private String name;

	@NotBlank(message = "Username must be provided")
	private String username;

	@NotBlank(message = "Email must be provided")
	private String email;

	@NotBlank(message = "Password must be provided")
	private String password;

	@NotBlank(message = "Gitlab address must be provided")
	private String address;

	@NotBlank(message = "Gitlab token must be provided")
	private String token;

	private String avatarUrl;

	private String createdAt;

	public UserDTO() {

	}

	public UserDTO(User user) {
		this.uboardIdentifier = user.getUboardIdentifier();
		this.name = user.getName();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.address = user.getAddress();
		this.avatarUrl = user.getAvatarUrl();
	}

	public String getUboardIdentifier() {
		return uboardIdentifier;
	}

	public void setUboardIdentifier(String uboardIdentifier) {
		this.uboardIdentifier = uboardIdentifier;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
}
