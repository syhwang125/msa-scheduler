package com.msa.scheduler.common.service;

import java.util.Properties;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.msa.scheduler.common.entity.SchedulerJobInfo;

@Service
//@RequiredArgsConstructor
public class KafkaMessageProducer {

	private static final String TOPIC = "test_topic";
	
	@Autowired
	private KafkaTemplate<String, SchedulerJobInfo> kafkaTemplate;
	
	@Value("{$spring.application.name}") 
	private String appName;
	
	public void sendMessage(SchedulerJobInfo message) {
		this.kafkaTemplate.send(TOPIC, message);
	}
	
	@Bean
	public NewTopic createTopic() {
		return new NewTopic(TOPIC, 3, (short) 1);
	}
	

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;
	
	@Value("${spring.kafka.producer.key-serializer}")
	private String keySerializer;
	
	@Value("${spring.kafka.producer.value-serializer}")
	private String valueSerializer;
	
	public void sendMessage(String payload) {
		Properties props = new Properties();
		props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
		props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
		
		KafkaProducer<String, String> producer = new KafkaProducer<>(props);
		ProducerRecord<String, String> message = new ProducerRecord<>("domain-event", payload);
		producer.send(message);
	}
	
/*
	public void produce(SchedulerJobInfo schedulerJobInfo) {
		Properties props = new Properties();
		props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
		props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
		
		KafkaProducer<String, String> producer = new KafkaProducer<>(props);
		ProducerRecord<String, String> message = new ProducerRecord<>("domain-event-job", schedulerJobInfo);
		producer.send(message);
	}
*/
}
