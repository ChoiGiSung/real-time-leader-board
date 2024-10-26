package com.coco.leaderboardcache

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {

    @Bean
    fun redisGameScoreTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, String> {
        val template = RedisTemplate<String, String>()
        template.connectionFactory = redisConnectionFactory
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = StringRedisSerializer()
        return template
    }

    @Bean
    fun redisLeaderboardCacheTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, LeaderboardCacheConsumer.LeaderBoardDto> {
        val template = RedisTemplate<String, LeaderboardCacheConsumer.LeaderBoardDto>()
        template.connectionFactory = redisConnectionFactory
        template.keySerializer = StringRedisSerializer()

        // ObjectMapper 설정
        val objectMapper = ObjectMapper().apply {
            registerModule(JavaTimeModule())  // Java 8 시간 API 지원
        }

        // Jackson2JsonRedisSerializer 사용
        val serializer = Jackson2JsonRedisSerializer(LeaderboardCacheConsumer.LeaderBoardDto::class.java)
        serializer.setObjectMapper(objectMapper)

        template.valueSerializer = serializer
        return template
    }
}