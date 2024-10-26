package com.coco.nosqlservice

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ConfigProperties(
) {

    @Bean
    @ConfigurationProperties(prefix = "kafka")
    fun kafkaProperties() = KafkaProperties()


    @Bean
    @ConfigurationProperties(prefix = "redis")
    fun redisProperties() = RedisProperties()


    class KafkaProperties {
        lateinit var gameScoreTopic: String
        lateinit var leaderboardScoreTopic: String
    }

    class RedisProperties {
        lateinit var host: String
        lateinit var port: String
        lateinit var leaderboardCollectionKey: String
    }
}