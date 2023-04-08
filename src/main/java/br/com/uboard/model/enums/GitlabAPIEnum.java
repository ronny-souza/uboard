package br.com.uboard.model.enums;

public enum GitlabAPIEnum {

	PROJECT("/project");

	private String path;

	GitlabAPIEnum(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}
