package br.com.uboard.rabbitmq.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.uboard.model.transport.SyncProjectDTO;
import br.com.uboard.rabbitmq.RabbitQueues;
import br.com.uboard.services.ProjectService;

@Component
public class SyncProjectsConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(SyncProjectsConsumer.class);

	private ProjectService projectService;

	public SyncProjectsConsumer(ProjectService projectService) {
		this.projectService = projectService;
	}

	@RabbitListener(queues = RabbitQueues.GITLAB_SYNC_PROJECTS)
	public void receive(SyncProjectDTO syncProjectDTO) {
		try {
			this.projectService.synchronizeProjects(syncProjectDTO);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}
