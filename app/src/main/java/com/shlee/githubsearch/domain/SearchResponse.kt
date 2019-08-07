package com.shlee.githubsearch.domain

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    @SerializedName("items") val users: List<User>
)