package com.shlee.githubsearch.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import com.jakewharton.rxrelay2.PublishRelay
import com.shlee.githubsearch.data.UserRepository
import com.shlee.githubsearch.data.db.Bookmark
import com.shlee.githubsearch.data.executor.NETWORK_IO
import com.shlee.githubsearch.domain.User
import com.shlee.githubsearch.extension.with
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class SearchViewModel(
    private val repository: UserRepository
) : BaseViewModel() {

    val userName = MutableLiveData<String>()
    val result = map(userName) {
        repository.searchByName(it, true, NETWORK_IO)
    }!!
    var searchItems = switchMap(result) { it.pagedList }!!
    var loadingState = switchMap(result) { it.loadingState }!!
    var refreshState = switchMap(result) { it.refreshState }!!

    private val updatedBookmarkId = MutableLiveData<Long>()
    var updatedBookmarkItemPosition = map(updatedBookmarkId) {
        searchItems.value?.indexOfFirst {
            it.id == updatedBookmarkId.value
        }
    }!!

    var bookmarkItems = MutableLiveData<MutableList<User>>().apply { value = arrayListOf() }
    var isBookmarkEmpty = map(bookmarkItems) { it.isEmpty() }!!

    private val autoCompletePublishSubject = PublishRelay.create<String>()

    init {
        configureAutoComplete()
    }

    @SuppressLint("CheckResult")
    private fun configureAutoComplete() {
        addToDisposable(autoCompletePublishSubject
            .debounce(300, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .filter { it.isNotEmpty() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                userName.value = it
            }, {
                // handle error
            })
        )
    }

    fun onQueryChanged(query: CharSequence) {
        autoCompletePublishSubject.accept(query.toString())
    }

    fun retrieveAllBookmark() {
        addToDisposable(repository.retrieveAllBookmark().with()
            .map {
                it.map { bookmark -> Bookmark.toUser(bookmark) }
            }
            .subscribe({
                bookmarkItems.value = it.toMutableList()
            }, {

            })
        )
    }

    fun addBookmark(user: User) {
        if (repository.isBookmarked(user)) {
            return
        }

        addToDisposable(repository.addBookmark(user)
            .flatMap {
                if (it > 0) {
                    repository.retrieveAllBookmark()
                } else {
                    Single.just(emptyList())
                }
            }
            .with()
            .subscribe({
                bookmarkItems.value = it.map { bookmark ->
                    Bookmark.toUser(bookmark)
                }.toMutableList()
                updatedBookmarkId.postValue(user.id)
            }, {

            }))
    }

    fun deleteBookmark(user: User) {
        addToDisposable(repository.deleteBookmark(Bookmark.fromUser(user))
            .flatMap {
                if (it > 0) {
                    repository.retrieveAllBookmark()
                } else {
                    Single.just(emptyList())
                }
            }
            .with()
            .subscribe({
                bookmarkItems.value = it.map { bookmark ->
                    Bookmark.toUser(bookmark)
                }.toMutableList()
                updatedBookmarkId.postValue(user.id)
            }, {

            })
        )
    }

    fun refresh() {
        result.value?.refresh?.invoke()
    }

}