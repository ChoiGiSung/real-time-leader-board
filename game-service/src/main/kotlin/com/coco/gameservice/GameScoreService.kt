package com.coco.gameservice

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class GameScoreService(
    private val configProperties: ConfigProperties,
    private val kafkaTemplate: KafkaTemplate<String, GameScoreDto>
) {

    @Scheduled(fixedRate = 1000)
    fun randomGameScore() {
        val batchSize = 2000
        for (i in 1..batchSize) {
            val gameScore = buildGameScore()
            kafkaTemplate.send(configProperties.kafkaProperties().gameScoreTopic, gameScore.userId.toString(), gameScore)
        }
    }

    private fun buildGameScore(): GameScoreDto {
        val random = Random()
        return GameScoreDto(
            random.nextLong(100) + 1,
            random.nextInt(1_000) + 1,
            Instant.now()
        )
    }

    inner class GameScoreDto(
        val userId: Long,
        val score: Int,
        val createdAt: Instant
    ) {

    }
}