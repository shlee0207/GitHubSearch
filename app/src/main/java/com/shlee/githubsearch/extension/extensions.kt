package com.shlee.githubsearch.extension

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.LiveData
import androidx.paging.*
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

fun <T> Single<T>.with() = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <Key, Value> DataSource.Factory<Key, Value>.toLiveData(
    pageSize: Int,
    initialLoadKey: Key? = null,
    boundaryCallback: PagedList.BoundaryCallback<Value>? = null,
    config: PagedList.Config = Config(pageSize),
    fetchExecutor: Executor = ArchTaskExecutor.getIOThreadExecutor()
): LiveData<PagedList<Value>> {
    return LivePagedListBuilder(this, config)
        .setInitialLoadKey(initialLoadKey)
        .setBoundaryCallback(boundaryCallback)
        .setFetchExecutor(fetchExecutor)
        .build()
}

fun <T> List<T>.cloneAndAddElement(element: T) : List<T> {
    val newList = ArrayList<T>()
    newList.addAll(this)
    newList.add(element)
    return newList
}

fun <T> List<T>.cloneAndRemoveElement(index: Int) : List<T> {
    if (index < 0 || index >= this.size) {
        return this
    }

    val newList = ArrayList<T>()
    newList.addAll(this)
    newList.removeAt(index)
    return newList
}