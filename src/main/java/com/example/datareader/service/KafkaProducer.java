package com.example.datareader.service;

import com.example.datareader.model.Student;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${message.topic.name}")
    private String msgTopicName;

    public void sendMessage(Student student) {
        kafkaTemplate.send(msgTopicName, student)
                .addCallback(
                        result -> log.info("Student name sent to topic: {}", student.getFirstName()),
                        ex -> log.error("Failed to send message", ex)
                );
    }

    public void sendMessage(String topicName, Student student) {
        kafkaTemplate.send(topicName, student)
                .addCallback(
                        result -> log.info("Student name sent to topic: {}", student.getFirstName()),
                        ex -> log.error("Failed to send message", ex)
                );
    }

}