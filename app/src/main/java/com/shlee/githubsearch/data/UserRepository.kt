package com.shlee.githubsearch.data

import androidx.paging.PagedList
import com.shlee.githubsearch.data.db.Bookmark
import com.shlee.githubsearch.data.network.Listing
import com.shlee.githubsearch.domain.User
import io.reactivex.Observable
import io.reactivex.Single

interface UserRepository {

    fun retrieveAllBookmark(): Single<List<Bookmark>>

    fun searchByName(name: String, isRemote: Boolean): Listing<User>
//    fun searchByName(name: String, isRemote: Boolean): Observable<PagedList<User>>

    fun addToBookmark(user: User): Single<Long>

    fun deleteBookmark(bookmark: Bookmark): Single<Int>

    fun isBookmarked(user: User): Boolean

}