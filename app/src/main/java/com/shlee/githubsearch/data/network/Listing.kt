package com.shlee.githubsearch.data.network

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class Listing<T>(
    val pagedList: LiveData<PagedList<T>>,
    val loadingState: LiveData<LoadingState>,
    val refreshState: LiveData<LoadingState>,
    val refresh: () -> Unit
)