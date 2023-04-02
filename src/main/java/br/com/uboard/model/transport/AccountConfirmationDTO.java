package br.com.uboard.model.transport;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;

public class AccountConfirmationDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "The code must be provided")
	private String code;

	@NotBlank(message = "Account email must be provided")
	private String email;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
