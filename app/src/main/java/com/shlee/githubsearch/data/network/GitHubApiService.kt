package com.shlee.githubsearch.data.network

import com.shlee.githubsearch.domain.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApiService {

    @GET("search/users")
    fun searchUsersByPage(@Query("q") name: String,
                    @Query("page") page: Int,
                    @Query("per_page") perPage: Int): Call<SearchResponse>

}