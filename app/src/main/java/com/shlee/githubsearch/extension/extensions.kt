package com.shlee.githubsearch.extension

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.LiveData
import androidx.paging.*
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor
import java.util.concurrent.Executors

fun <T> Single<T>.with() = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())



/*fun <Key, Value> DataSource.Factory<Key, Value>.toObservable(
    pageSize: Int,
    initialLoadKey: Key? = null,
    boundaryCallback: PagedList.BoundaryCallback<Value>? = null,
    schedulers: Scheduler = Schedulers.io()
): Observable<PagedList<Value>> {
    return RxPagedListBuilder(this, Config(pageSize))
            .setInitialLoadKey(initialLoadKey)
            .setBoundaryCallback(boundaryCallback)
            .setFetchScheduler(schedulers)
            .buildObservable()
}*/

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