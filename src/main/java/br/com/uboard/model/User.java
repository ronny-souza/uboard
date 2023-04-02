package br.com.uboard.model;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.uboard.model.transport.UserDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "uboardIdentifier", nullable = false, unique = true)
	private String uboardIdentifier;

	@Column(name = "gitlabIdentifier", nullable = false, unique = true)
	private Long gitlabIdentifier;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(columnDefinition = "tinyint DEFAULT 0")
	private boolean enabled;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	private String token;

	@Column(name = "avatarUrl", nullable = true)
	private String avatarUrl;

	public User() {

	}

	public User(UserDTO userDTO) {
		this.uboardIdentifier = UUID.randomUUID().toString();
		this.name = userDTO.getName();
		this.username = userDTO.getUsername();
		this.email = userDTO.getEmail();
		this.gitlabIdentifier = userDTO.getId();
		this.enabled = false;
		this.address = userDTO.getAddress();
		this.token = userDTO.getToken();
		this.avatarUrl = userDTO.getAvatarUrl();
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	@Override
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.enabled;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
}
