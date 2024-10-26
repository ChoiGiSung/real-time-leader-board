package com.coco.gameservice

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ConfigProperties(
) {

    @Bean
    @ConfigurationProperties(prefix = "kafka-properties")
    fun kafkaProperties() = KafkaProperties()


    class KafkaProperties {
        lateinit var gameScoreTopic: String
    }
}