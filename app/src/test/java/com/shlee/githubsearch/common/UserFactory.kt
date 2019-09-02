package com.shlee.githubsearch.common

import com.shlee.githubsearch.domain.User
import java.util.concurrent.atomic.AtomicInteger

class UserFactory {

    private val counter = AtomicInteger(0)
    fun createUser(): User {
        val id = counter.incrementAndGet()
        return User(
            id = id.toLong(),
            name = "name_$id",
            avatarUrl = "",
            isBookmarked = false
        )
    }

}