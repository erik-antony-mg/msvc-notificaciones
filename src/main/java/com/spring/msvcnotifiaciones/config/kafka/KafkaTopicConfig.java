package com.spring.msvcnotifiaciones.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    @Value("${kafka.topic.name}")
    private String topicName;

    @Bean
    public NewTopic createTopic(){
        Map<String,String> configurationTopic=new HashMap<>();
        configurationTopic.put(TopicConfig.CLEANUP_POLICY_CONFIG,TopicConfig.CLEANUP_POLICY_DELETE);
        configurationTopic.put(TopicConfig.RETENTION_MS_CONFIG,"600000");
        configurationTopic.put(TopicConfig.SEGMENT_BYTES_CONFIG,"1073741824");
        configurationTopic.put(TopicConfig.MAX_MESSAGE_BYTES_CONFIG,"2048000");

        return TopicBuilder.name(topicName)
                .partitions(2)
                .replicas(2)
                .configs(configurationTopic)
                .build();
    }
}
