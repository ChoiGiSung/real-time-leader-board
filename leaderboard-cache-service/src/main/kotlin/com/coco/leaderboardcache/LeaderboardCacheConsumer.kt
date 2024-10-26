package com.coco.leaderboardcache

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.time.Instant

@Component
@KafkaListener(
    topics = ["\${kafka.leaderboard-change-topic}"],
    groupId = "cache-\${kafka.leaderboard-change-topic}"
)
class LeaderboardCacheConsumer(
    private val configProperties: ConfigProperties,
    private val redisGameScoreTemplate: RedisTemplate<String, String>,
//    private val redisUserCacheTemplate: RedisTemplate<String, String>,
    private val redisLeaderboardCacheTemplate: RedisTemplate<String, LeaderBoardDto>,
//    private val kafkaLeaderboardTemplate: KafkaTemplate<String, LeaderBoardDto>,
) {

    @KafkaHandler
    fun leaderboardCacheConsumer(record: LeaderBoardDto) {
        val leaderBoardCollectionKey = configProperties.redisProperties().leaderboardCollectionKey
        val leaderBoardCacheCollectionKey = configProperties.redisProperties().leaderboardCacheCollectionKey

        val leaderBoardCache = redisLeaderboardCacheTemplate.opsForValue().get(leaderBoardCacheCollectionKey)
        val now = Instant.now()

        if (leaderBoardCache != null && !canRefreshLeaderboard(leaderBoardCache, now)) {
            return
        }

        // get current leaderboard top 10 users from sortedset and  update leaderboard cache & send this update to "leaderboard" topic
        val leaderboard: Set<ZSetOperations.TypedTuple<String>> =
            redisGameScoreTemplate.opsForZSet().reverseRangeWithScores(leaderBoardCollectionKey, 0, 9) ?: emptySet()


        val leaderboardUpdate = mapToLeaderBoardDto(leaderboard, now)

        //update leaderboard cache which stores only top 10 users
        redisLeaderboardCacheTemplate.opsForValue().set(leaderBoardCacheCollectionKey, leaderboardUpdate)

        //publish leaderBoard updated record to "leaderboard"  kafka topic
//        kafkaLeaderboardTemplate.send("leaderboard", leaderboardUpdate)

    }

    private fun canRefreshLeaderboard(leaderBoardCache: LeaderBoardDto, now: Instant): Boolean {
        val throttleThreshold = now.minusMillis(configProperties.redisProperties().leaderboardCacheThrottleThreshold)
        return leaderBoardCache.createdAt.isBefore(throttleThreshold)
    }

    private fun mapToLeaderBoardDto(
        leaderBoard: Set<ZSetOperations.TypedTuple<String>>,
        now: Instant
    ): LeaderBoardDto {
        val userList = mutableListOf<User>()
        for ((rank, tuple) in leaderBoard.withIndex()) {
            val userId = tuple.value!!.toLong()
            val user = User(userId, rank + 1, tuple.score!!.toInt())
            userList.add(user)
        }
        return LeaderBoardDto(userList, now)
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
