package com.coco.leaderboardcache

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ConfigProperties(
) {

//    @Bean
//    @ConfigurationProperties(prefix = "kafka")
//    fun kafkaProperties() = KafkaProperties()


    @Bean
    @ConfigurationProperties(prefix = "redis")
    fun redisProperties() = RedisProperties()

//
//    class KafkaProperties {
//        lateinit var leaderboardChangeTopic: String
//    }

    class RedisProperties {
        lateinit var host: String
        lateinit var port: String
        lateinit var leaderboardCollectionKey: String
        lateinit var leaderboardCacheCollectionKey: String
        val leaderboardCacheThrottleThreshold: Long by lazy { 500 }
    }
}
