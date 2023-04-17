package br.com.uboard.rabbitmq;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMQConfiguration {

	private static final String X_MESSAGE_TTL = "x-message-ttl";
	private AmqpAdmin amqpAdmin;

	public RabbitMQConfiguration(AmqpAdmin amqpAdmin) {
		this.amqpAdmin = amqpAdmin;
	}

	@Bean
	public Jackson2JsonMessageConverter converter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public Queue syncGitlabUserCredentialsQueue() {
		Queue queue = QueueBuilder.durable(RabbitQueues.GITLAB_USER_CREDENTIALS)
				.withArgument(X_MESSAGE_TTL, TimeUnit.MINUTES.toMillis(10)).build();
		this.amqpAdmin.declareQueue(queue);
		return queue;
	}

	@Bean
	public Queue sendMailQueue() {
		Queue queue = QueueBuilder.durable(RabbitQueues.SEND_MAIL)
				.withArgument(X_MESSAGE_TTL, TimeUnit.MINUTES.toMillis(10)).build();
		this.amqpAdmin.declareQueue(queue);
		return queue;
	}

	@Bean
	public Queue requestGitlabSyncGroupMilestonesQueue() {
		Queue queue = QueueBuilder.durable(RabbitQueues.GITLAB_REQUEST_SYNC_GROUP_MILESTONES)
				.withArgument(X_MESSAGE_TTL, TimeUnit.MINUTES.toMillis(10)).build();
		this.amqpAdmin.declareQueue(queue);
		return queue;
	}

	@Bean
	public Queue requestGitlabSyncProjectMilestonesQueue() {
		Queue queue = QueueBuilder.durable(RabbitQueues.GITLAB_REQUEST_SYNC_PROJECT_MILESTONES)
				.withArgument(X_MESSAGE_TTL, TimeUnit.MINUTES.toMillis(10)).build();
		this.amqpAdmin.declareQueue(queue);
		return queue;
	}
}
