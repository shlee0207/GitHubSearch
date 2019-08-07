package com.shlee.githubsearch.domain

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Long,
    @SerializedName("login") val name: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    var isBookmarked: Boolean
)