package com.ezedin.auth_service.configuration;

import com.ezedin.auth_service.model.dto.studentRegisteredEvent;
import com.ezedin.auth_service.model.dto.teacherRegisteredEvent;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class configuration {
    @Bean
    public NewTopic studentRegistrationTopic() {
        return TopicBuilder.name("student-registration-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic teacherRegistrationTopic() {
        return TopicBuilder.name("teacher-registration-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
    public ProducerFactory<String, studentRegisteredEvent> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // change if needed
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, studentRegisteredEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    public ProducerFactory<String, teacherRegisteredEvent> teacherProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // change if needed
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, teacherRegisteredEvent> teacherkafkaTemplate() {
        return new KafkaTemplate<>(teacherProducerFactory());
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
