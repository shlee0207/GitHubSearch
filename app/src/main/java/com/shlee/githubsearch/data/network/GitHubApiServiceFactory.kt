package com.shlee.githubsearch.data.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object GitHubApiServiceFactory {

    private const val BASE_URL = "https://api.github.com/"

    fun makeGitHubApiService(): GitHubApiService {
        return makeGitHubApiService(
            makeOkHttpClient(),
            makeGson()
        )
    }

    fun makeGitHubApiService(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): GitHubApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(GitHubApiService::class.java)
    }

    private fun makeOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    private fun makeGson(): Gson {
        return GsonBuilder()
            .create()
    }

}