package br.com.uboard.rabbitmq.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.uboard.model.transport.SyncMilestoneDTO;
import br.com.uboard.rabbitmq.RabbitQueues;
import br.com.uboard.services.MilestoneService;

@Component
public class SyncProjectMilestonesConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(SyncProjectMilestonesConsumer.class);

	private MilestoneService milestoneService;

	public SyncProjectMilestonesConsumer(MilestoneService milestoneService) {
		this.milestoneService = milestoneService;
	}

	@RabbitListener(queues = RabbitQueues.GITLAB_SYNC_PROJECT_MILESTONES)
	public void receive(SyncMilestoneDTO syncMilestoneDTO) {
		try {
			this.milestoneService.synchronizeProjectMilestones(syncMilestoneDTO);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}
