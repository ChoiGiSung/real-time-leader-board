package com.coco.leaderboardws

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ConfigProperties(
) {

    @Bean
    @ConfigurationProperties(prefix = "kafka")
    fun kafkaProperties() = KafkaProperties()

    class KafkaProperties {
        lateinit var leaderboardTopic: String
    }

}
