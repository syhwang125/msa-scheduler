package com.msa.scheduler.common.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.MessageHeaders;
//import org.springframework.messaging.handler.annotation.Headers;
//import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.msa.scheduler.common.entity.SchedulerJobInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaMessageConsumer {
	
//	@KafkaListener(topics = "test_topic", groupId = "group_id")
//	public void consumeMessage(String message) {
//		System.out.println(">>> Received Message : " + message);
//		log.debug(">>> Received Message : " + message);
//	}
	

	@KafkaListener(topics = "quartz-event")
	public void consumeMessage2(@Headers MessageHeaders headers, @Payload String message) {
		log.debug("Received Headers : " + headers);
		log.debug("Received Payloads : " + message);
	}
	

	@KafkaListener(topics = "domain-event-job", containerFactory = "kafkaListenerContainerFactory") 
	public void listenDomainEvent(@Headers MessageHeaders headers, @Payload SchedulerJobInfo schedulerJobInfo) {
		log.debug("Received Headers : " + headers);
		log.debug("Received Payloads : " + schedulerJobInfo.toString());
	}

}
