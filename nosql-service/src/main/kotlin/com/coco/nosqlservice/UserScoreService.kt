package com.coco.nosqlservice

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.time.Instant

@Component
@KafkaListener(
    topics = ["\${kafka.game-score-topic}"],
    groupId = "nosql-\${kafka.game-score-topic}"
)
class UserScoreService(
    private val configProperties: ConfigProperties,
    private val redisTemplate: RedisTemplate<String, String>,
    private val kafkaTemplate: KafkaTemplate<String, String>,
) {

    @KafkaHandler
    fun consumeGameScore(record: UserScoreDto) {
        val score = record.score
        val leaderBoardCollectionKey = configProperties.redisProperties().leaderboardCollectionKey

        redisTemplate.opsForZSet()
            .incrementScore(leaderBoardCollectionKey, record.userId.toString(), score.toDouble())

        kafkaTemplate.send(
            configProperties.kafkaProperties().leaderboardScoreTopic,
            LeaderBoardDto(record.createdAt).toString()
        )
    }


    data class UserScoreDto(
        val userId: Long,
        val score: Int,
        val createdAt: Instant
    )

    data class LeaderBoardDto(
        val createdAt: Instant
    )
}
