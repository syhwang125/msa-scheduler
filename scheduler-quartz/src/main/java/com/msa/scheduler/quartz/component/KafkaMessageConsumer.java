package com.msa.scheduler.quartz.component;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.msa.scheduler.quartz.entity.SchedulerJobInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaMessageConsumer {
	@KafkaListener(topics = "quartz-event")
	public void consumeMessage(@Headers MessageHeaders headers, @Payload String message) {
		log.debug("Received Headers : " + headers);
		log.debug("Received Payloads : " + message);
	}
	
	@KafkaListener(topics = "domain-event-job", containerFactory = "kafkaListenerContainerFactory") 
	public void listenDomainEvent(@Headers MessageHeaders headers, @Payload SchedulerJobInfo schedulerJobInfo) {
		log.debug("Received Headers : " + headers);
		log.debug("Received Payloads : " + schedulerJobInfo.toString());
	}

}
