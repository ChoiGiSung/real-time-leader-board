package com.coco.leaderboardws

import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component
import java.time.Instant

@Component
@KafkaListener(
    topics = ["\${kafka.leaderboard-topic}"],
    groupId = "\${kafka.leaderboard-topic}"
)
class LeaderBoardConsumer(
    private val simpMessagingTemplate: SimpMessagingTemplate,
) {

    @KafkaHandler
    fun leaderboardCacheConsumer(record: LeaderBoardDto) {
        simpMessagingTemplate.convertAndSend("/live-updates/leaderboard", record)
    }

    data class LeaderBoardDto(
        val users: List<User>?,
        val createdAt: Instant
    ) {
        constructor() : this(null, Instant.now())
    }

    data class User(
        val userId: Long,
        val rank: Int,
        val score: Int
//        val nickname: String
    ) {
        // 기본 생성자 추가
        constructor() : this(0L, 0, 0)
    }
}
