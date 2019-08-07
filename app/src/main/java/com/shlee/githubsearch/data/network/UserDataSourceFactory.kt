package com.shlee.githubsearch.data.network

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.shlee.githubsearch.domain.User

class UserDataSourceFactory(
    private val api: GitHubApiService,
    private val name: String
) : DataSource.Factory<Int, User>() {
    val sourceLiveData = MutableLiveData<PageKeyedUserDataSource>()
    override fun create(): DataSource<Int, User> {
        val source = PageKeyedUserDataSource(api, name)
        sourceLiveData.postValue(source)
        return source
    }

}