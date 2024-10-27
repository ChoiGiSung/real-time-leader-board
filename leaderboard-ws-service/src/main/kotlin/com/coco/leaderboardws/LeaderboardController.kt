package com.coco.leaderboardws

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LeaderboardController {

    @GetMapping("/leaderboard")
    fun leaderboard(): String {
        return "leaderboard"
    }
}