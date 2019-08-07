package com.shlee.githubsearch.data.network

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.shlee.githubsearch.domain.SearchResponse
import com.shlee.githubsearch.domain.User
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

class PageKeyedUserDataSource(
    private val api : GitHubApiService,
    private val name: String
) : PageKeyedDataSource<Int, User>() {

    var networkState = MutableLiveData<LoadingState>()
    var refreshState = MutableLiveData<LoadingState>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, User>
    ) {
        val request = api.searchUsersByPage(
            name,
            1,
            params.requestedLoadSize
        )

        networkState.postValue(LoadingState.LOADING)
        refreshState.postValue(LoadingState.LOADING)

        try {
            val response = request.execute()
            val items = response.body() ?. users ?: emptyList()

            refreshState.postValue(LoadingState.LOADED)

            callback.onResult(items, null, PageLinks(response.headers()).nextPageKey)
        } catch(ioException: IOException) {

        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, User>
    ) {}

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, User>
    ) {
        networkState.postValue(LoadingState.LOADING)
        api.searchUsersByPage(
            name,
            params.key,
            params.requestedLoadSize
        ).enqueue(
            object : retrofit2.Callback<SearchResponse> {
                override fun onFailure(
                    call: Call<SearchResponse>,
                    t: Throwable
                ) {

                }

                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    if (response.isSuccessful) {
                        val items = response.body() ?. users ?: emptyList()

                        networkState.postValue(LoadingState.LOADED)
                        callback.onResult(items, PageLinks(response.headers()).nextPageKey)
                    }
                }
            }
        )
    }

}