package br.com.uboard.rabbitmq.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.uboard.model.transport.SyncMilestoneDTO;
import br.com.uboard.rabbitmq.RabbitQueues;
import br.com.uboard.services.MilestoneService;

@Component
public class SyncGroupMilestonesConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(SyncGroupMilestonesConsumer.class);

	private MilestoneService milestoneService;

	public SyncGroupMilestonesConsumer(MilestoneService milestoneService) {
		this.milestoneService = milestoneService;
	}

	@RabbitListener(queues = RabbitQueues.GITLAB_SYNC_GROUP_MILESTONES)
	public void receive(SyncMilestoneDTO syncMilestoneDTO) {
		try {
			this.milestoneService.synchronizeGroupMilestones(syncMilestoneDTO);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}
