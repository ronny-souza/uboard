package br.com.uboard.model.transport;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;

public class CredentialsDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Gitlab address must be provided")
	private String address;

	@NotBlank(message = "Gitlab user token must be provided")
	private String token;

	private Long userUUID;

	private boolean removable;

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

	public Long getUserUUID() {
		return userUUID;
	}

	public void setUserUUID(Long userUUID) {
		this.userUUID = userUUID;
	}

	public boolean getRemovable() {
		return removable;
	}

	public void setRemovable(boolean removable) {
		this.removable = removable;
	}

}
