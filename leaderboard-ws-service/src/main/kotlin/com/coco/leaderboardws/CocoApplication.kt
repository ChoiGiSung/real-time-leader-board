package com.coco.leaderboardws

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
class CocoApplication

fun main(args: Array<String>) {
    runApplication<CocoApplication>(*args)
}
