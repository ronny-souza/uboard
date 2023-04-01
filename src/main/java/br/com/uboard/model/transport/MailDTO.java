package br.com.uboard.model.transport;

import java.io.Serializable;
import java.util.Map;

import br.com.uboard.model.enums.MailTypeEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class MailDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Recipient is required for sending email")
	@Email
	private String to;

	@NotBlank(message = "Subject is required for sending email")
	private String subject;

	private MailTypeEnum mailType;

	Map<String, Object> properties;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public MailTypeEnum getMailType() {
		return mailType;
	}

	public void setMailType(MailTypeEnum mailType) {
		this.mailType = mailType;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

}
