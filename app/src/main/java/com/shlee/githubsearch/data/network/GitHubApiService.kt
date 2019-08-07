package com.shlee.githubsearch.data.network

import com.shlee.githubsearch.domain.SearchResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface GitHubApiService {

    @GET("search/users")
    fun searchUsers(@QueryMap params: MutableMap<String, String>): Observable<SearchResponse>

    @GET("search/users")
    fun searchUsersByPage(@Query("q") name: String,
                    @Query("page") page: Int,
                    @Query("per_page") perPage: Int): Call<SearchResponse>

}