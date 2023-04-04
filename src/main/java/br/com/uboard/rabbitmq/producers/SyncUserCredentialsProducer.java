package br.com.uboard.rabbitmq.producers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.uboard.model.transport.CredentialsDTO;
import br.com.uboard.rabbitmq.MessageService;
import br.com.uboard.rabbitmq.RabbitQueues;
import br.com.uboard.services.UserService;

@Component
@EnableAsync
public class SyncUserCredentialsProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(SyncUserCredentialsProducer.class);

	private UserService userService;

	private MessageService messageService;

	public SyncUserCredentialsProducer(UserService userService, MessageService messageService) {
		this.userService = userService;
		this.messageService = messageService;
	}

	@Async
	@Scheduled(initialDelay = 60000, fixedDelay = 180000)
	public void send() {
		try {
			List<CredentialsDTO> credentials = this.userService.listCredentialsByUserEnabled();

			if (credentials != null && !credentials.isEmpty()) {
				this.messageService.enqueue(RabbitQueues.GITLAB_USER_CREDENTIALS, credentials);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}
