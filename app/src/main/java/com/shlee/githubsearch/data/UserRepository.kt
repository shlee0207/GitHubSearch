package com.shlee.githubsearch.data

import com.shlee.githubsearch.data.db.Bookmark
import com.shlee.githubsearch.data.network.Listing
import com.shlee.githubsearch.domain.User
import io.reactivex.Single
import java.util.concurrent.Executor

interface UserRepository {

    fun searchByName(name: String, isRemote: Boolean, executor: Executor): Listing<User>

    fun retrieveAllBookmark(): Single<List<Bookmark>>

    fun addBookmark(user: User): Single<Long>

    fun deleteBookmark(bookmark: Bookmark): Single<Int>

    fun isBookmarked(user: User): Boolean

}