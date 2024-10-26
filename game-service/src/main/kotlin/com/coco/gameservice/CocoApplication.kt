package com.coco.gameservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class CocoApplication

fun main(args: Array<String>) {
    runApplication<CocoApplication>(*args)
}
