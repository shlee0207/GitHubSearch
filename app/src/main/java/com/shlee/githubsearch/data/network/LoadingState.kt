package com.shlee.githubsearch.data.network

enum class Status {
    RUNNING,
    SUCCESS,
    FAIL
}

@Suppress("DataClassPrivateConstructor")
data class LoadingState private constructor(
    val status: Status,
    val msg: String? = null
) {
    companion object {
        val LOADED = LoadingState(Status.RUNNING)
        val LOADING = LoadingState(Status.SUCCESS)
        fun error(msg: String?) = LoadingState(Status.FAIL, msg)
    }
}