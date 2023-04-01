package br.com.uboard.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

	private RabbitTemplate rabbitTemplate;

	public MessageService(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void enqueue(String queueName, Object message) {
		this.convertAndSend(queueName, message);
	}

	private void convertAndSend(String queueName, Object message) {
		this.rabbitTemplate.convertAndSend(queueName, message);
	}
}
