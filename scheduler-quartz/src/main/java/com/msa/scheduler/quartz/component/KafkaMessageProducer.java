package com.msa.scheduler.quartz.component;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.msa.scheduler.quartz.entity.SchedulerJobInfo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaMessageProducer {

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
	
	public void sendMessage(SchedulerJobInfo schedulerJobInfo) {
		Properties props = new Properties();
		props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
		props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
		
		KafkaProducer<String, String> producer = new KafkaProducer<>(props);
		ProducerRecord<String, String> message = new ProducerRecord<>("domain-event-job", schedulerJobInfo);
		producer.send(message);
	}
}
