package com.shlee.githubsearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.paging.Config
import androidx.paging.PagedList
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.shlee.githubsearch.common.FakeGitHubApi
import com.shlee.githubsearch.common.RxSchedulerRule
import com.shlee.githubsearch.data.UserRepository
import com.shlee.githubsearch.data.executor.NETWORK_IO
import com.shlee.githubsearch.data.network.Listing
import com.shlee.githubsearch.data.network.LoadingState
import com.shlee.githubsearch.data.network.UserDataSourceFactory
import com.shlee.githubsearch.domain.User
import com.shlee.githubsearch.extension.toLiveData
import com.shlee.githubsearch.viewmodels.SearchViewModel
import org.hamcrest.CoreMatchers.`is`
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import java.util.concurrent.Executor

class SearchViewModelTest {

    @JvmField @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @JvmField @Rule
    val rxSchedulerRule = RxSchedulerRule()

    private lateinit var repository: UserRepository
    private lateinit var viewModel: SearchViewModel

    private var userNameObserver: Observer<String> = mock()
    private var resultObserver: Observer<Listing<User>> = mock()
    private var searchItemObserver: Observer<PagedList<User>> = mock()
    private var refreshStateObserver: Observer<LoadingState> = mock()

    private var fakeGitHubApi = FakeGitHubApi()

    private val executor = Executor { command -> command.run() }

    @Before
    fun setUp() {
        repository = mock()
        viewModel = SearchViewModel(repository)

        viewModel.userName.observeForever(userNameObserver)
        viewModel.result.observeForever(resultObserver)
        viewModel.searchItems.observeForever(searchItemObserver)
        //viewModel.refreshState.observeForever(refreshStateObserver)
    }

    @Test
    fun loadSearchItemsWhenQueryChanged() {
        stubRepository()
        viewModel.onQueryChanged("foo")
        verify(userNameObserver).onChanged("foo")
        assertThat(viewModel.searchItems.value!!.size, `is`(30))
    }

    private fun stubRepository() {
        val name = "foo"
        whenever(repository.searchByName(name, true, NETWORK_IO))
            .thenReturn(listing(name))
    }

    private fun listing(name: String): Listing<User> {
        fakeGitHubApi.addUser(30)
        val sourceFactory = UserDataSourceFactory(fakeGitHubApi, name)
        val livePagedList = sourceFactory.toLiveData(
            pageSize = 30,
            config = Config(pageSize = 30, initialLoadSizeHint = 30),
            fetchExecutor = executor)

        return Listing(
            pagedList = livePagedList,
            loadingState = Transformations.switchMap(sourceFactory.sourceLiveData) { it.networkState },
            refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) { it.refreshState },
            refresh = { sourceFactory.sourceLiveData.value?.invalidate() }
        )
    }

}
