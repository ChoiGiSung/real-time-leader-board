package com.coco.dbservice

import org.springframework.data.jpa.repository.JpaRepository

interface UserScoreRepository: JpaRepository<UserScore, Long> {
}