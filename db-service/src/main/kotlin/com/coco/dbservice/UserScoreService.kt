package com.coco.dbservice

import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.time.Instant

@Component
@KafkaListener(
    topics = ["\${kafka.game-score-topic}"],
    groupId = "rdb-\${kafka.game-score-topic}"
)
class UserScoreService(
    private val userScoreRepository: UserScoreRepository
) {

    @KafkaHandler
    fun consumeGameScore(record: UserScoreDto) {
        val gameScore = record.toUserScore()
        userScoreRepository.save(gameScore)
    }


    data class UserScoreDto(
        val userId: Long,
        val score: Int,
        val createdAt: Instant
    ) {

        fun toUserScore() = UserScore(
            userId = userId,
            score = score,
            createdAt = createdAt
        )

    }
}