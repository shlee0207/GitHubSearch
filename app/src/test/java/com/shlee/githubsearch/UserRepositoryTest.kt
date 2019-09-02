package com.shlee.githubsearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.shlee.githubsearch.common.FakeGitHubApi
import com.shlee.githubsearch.common.UserFactory
import com.shlee.githubsearch.data.UserRepository
import com.shlee.githubsearch.data.UserRepositoryImpl
import com.shlee.githubsearch.data.cache.BookmarkCache
import com.shlee.githubsearch.data.db.Bookmark
import com.shlee.githubsearch.data.db.BookmarkDao
import com.shlee.githubsearch.domain.User
import io.reactivex.Single
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.Executor

class UserRepositoryTest {

    @JvmField @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var fakeGitHubApi = FakeGitHubApi()
    private var bookmarkDao: BookmarkDao = mock()
    private var bookmarkCache:BookmarkCache = mock()

    private lateinit var userRepository: UserRepository

    private val executor = Executor { command -> command.run() }

    private val observer = LoggingObserver<PagedList<User>>()

    @Before
    fun setup() {
        userRepository = UserRepositoryImpl.getInstance(
            bookmarkDao = bookmarkDao,
            gitHubApiService = fakeGitHubApi,
            bookmarkCache = bookmarkCache
        )
    }

    @After
    fun tearDown() {
        val instance = userRepository::class.java.getDeclaredField("instance")
        instance.isAccessible = true
        instance.set(null, null)
    }

    @Test
    fun empty() {
        val listing = userRepository.searchByName("foo", true, executor)
        listing.pagedList.observeForever(
            observer
        )

        assertThat(observer.value!!.size, `is`(0))
    }

    @Test
    fun oneItem() {
        fakeGitHubApi.addUser(1)
        val listing = userRepository.searchByName("foo", true, executor)
        listing.pagedList.observeForever(
            observer
        )

        assertThat(observer.value!!.size, `is`(1))
    }

    @Test
    fun testRetrieveAllBookmark() {
        val bookmarkList = mock<List<Bookmark>>()
        whenever(bookmarkDao.selectAll()).thenReturn(
            Single.just(bookmarkList)
        )
        userRepository.retrieveAllBookmark().test()
        verify(bookmarkCache).saveBookmarks(bookmarkList)
    }

    @Test
    fun testAddBookmarkWhenSuccess() {
        val userFactory = UserFactory()
        val user = userFactory.createUser()
        val bookmark = Bookmark.fromUser(user)
        whenever(bookmarkDao.insert(bookmark)).thenReturn(
            Single.just(user.id)
        )
        userRepository.addBookmark(user).test()
        verify(bookmarkCache).saveBookmark(bookmark)
    }

    @Test
    fun testAddBookmarkWhenFail() {
        val user = UserFactory().createUser()
        val bookmark = Bookmark.fromUser(user)
        whenever(bookmarkDao.insert(bookmark)).thenReturn(
            Single.just(0)
        )
        userRepository.addBookmark(user).test()
        verify(bookmarkCache, times(0)).saveBookmark(bookmark)
    }

    @Test
    fun testDeleteBookmarkWhenSuccess() {
        val user = UserFactory().createUser()
        val bookmark = Bookmark.fromUser(user)
        whenever(bookmarkDao.delete(bookmark.uid)).thenReturn(
            Single.just(1)
        )
        userRepository.deleteBookmark(bookmark).test()
        verify(bookmarkCache).removeBookmark(bookmark)
    }

    private class LoggingObserver<T> : Observer<T> {
        var value : T? = null
        override fun onChanged(t: T) {
            this.value = t
        }
    }

}