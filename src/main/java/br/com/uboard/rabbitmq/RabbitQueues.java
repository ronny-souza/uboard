package br.com.uboard.rabbitmq;

public class RabbitQueues {

	public static final String GITLAB_USER_CREDENTIALS = "GITLAB.USER.CREDENTIALS";
	public static final String SEND_MAIL = "SEND.MAIL";
	public static final String GITLAB_SYNC_PROJECTS = "GITLAB.SYNC.PROJECTS";
	public static final String GITLAB_SYNC_GROUP_MILESTONES = "GITLAB.SYNC.GROUP.MILESTONES";
	public static final String GITLAB_SYNC_PROJECT_MILESTONES = "GITLAB.SYNC.PROJECT.MILESTONES";
	public static final String GITLAB_REQUEST_SYNC_GROUP_MILESTONES = "GITLAB.REQUEST.SYNC.GROUP.MILESTONES";
	public static final String GITLAB_REQUEST_SYNC_PROJECT_MILESTONES = "GITLAB.REQUEST.SYNC.PROJECT.MILESTONES";

	private RabbitQueues() {

	}
}
