package com.msa.scheduler.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.msa.scheduler.common.entity.SchedulerJobInfo;
import com.msa.scheduler.common.service.KafkaMessageProducer;

@RestController
public class KafkaController {

	private final KafkaMessageProducer producer;
	
	@Autowired
	public KafkaController(KafkaMessageProducer producer) {
		this.producer = producer;
	}
	
	@PostMapping("/publish") 
	public void messageToTopic(@RequestParam("message") SchedulerJobInfo message) {
		this.producer.sendMessage(message);
	}
	
	
}
