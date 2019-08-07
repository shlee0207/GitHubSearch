package com.shlee.githubsearch.data

import android.content.Context
import androidx.lifecycle.Transformations
import androidx.lifecycle.Transformations.switchMap
import androidx.paging.Config
import com.shlee.githubsearch.data.cache.BookmarkCache
import com.shlee.githubsearch.data.db.Bookmark
import com.shlee.githubsearch.data.db.BookmarkDao
import com.shlee.githubsearch.data.executor.NETWORK_IO
import com.shlee.githubsearch.data.network.GitHubApiService
import com.shlee.githubsearch.data.network.Listing
import com.shlee.githubsearch.data.network.UserDataSourceFactory
import com.shlee.githubsearch.domain.User
import com.shlee.githubsearch.extension.toLiveData
import com.shlee.githubsearch.util.provideBookmarkCache
import com.shlee.githubsearch.util.provideBookmarkDao
import com.shlee.githubsearch.util.provideGitHubApiService
import io.reactivex.Single

class UserRepositoryImpl private constructor(
    private val bookmarkDao: BookmarkDao,
    private val gitHubApiService: GitHubApiService,
    private val bookmarkCache: BookmarkCache
): UserRepository {

    override fun retrieveAllBookmark(): Single<List<Bookmark>> {
        return bookmarkDao.selectAll().doOnSuccess {
            it.forEach { bookmark ->
                bookmarkCache.saveToCache(bookmark)
            }
        }
    }

    override fun searchByName(name: String, isRemote: Boolean): Listing<User> =
        if (isRemote) {
            searchFromRemote(name)
        } else {
            searchFromDb(name)
        }

    private fun searchFromRemote(name: String): Listing<User> {
        val sourceFactory = UserDataSourceFactory(gitHubApiService, name)
        val livePagedList = sourceFactory.toLiveData(
            pageSize = 30,
            config = Config(pageSize = 30, initialLoadSizeHint = 30),
            fetchExecutor = NETWORK_IO)

        return Listing(
            pagedList = livePagedList,
            loadingState = switchMap(sourceFactory.sourceLiveData) { it.networkState },
            refreshState = switchMap(sourceFactory.sourceLiveData) { it.refreshState },
            refresh = { sourceFactory.sourceLiveData.value?.invalidate() }
        )
    }

    private fun searchFromDb(name: String): Listing<User> {
        TODO("not implemented")
    }

    override fun addToBookmark(user: User): Single<Long> {
        val bookmark = Bookmark.fromUser(user)
        return bookmarkDao.insert(bookmark).doOnSuccess {
            if (it > 0) {
                bookmarkCache.saveToCache(bookmark)
            }
        }
    }

    override fun deleteBookmark(bookmark: Bookmark): Single<Int> {
        return bookmarkDao.delete(bookmark.uid).doOnSuccess {
            if (it > 0) {
                bookmarkCache.removeBookmark(bookmark)
            }
        }
    }

    override fun isBookmarked(user: User): Boolean {
        return bookmarkCache.isBookmarked(user)
    }

    companion object {

        private var instance: UserRepository? = null

        fun getInstance(context: Context): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepositoryImpl(
                                provideBookmarkDao(context),
                                provideGitHubApiService(),
                                provideBookmarkCache()
                            ).also { instance = it }
            }

    }
}