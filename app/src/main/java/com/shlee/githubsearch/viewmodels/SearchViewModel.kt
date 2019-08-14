package com.shlee.githubsearch.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import com.jakewharton.rxrelay2.PublishRelay
import com.shlee.githubsearch.data.UserRepository
import com.shlee.githubsearch.data.db.Bookmark
import com.shlee.githubsearch.domain.User
import com.shlee.githubsearch.extension.cloneAndAddElement
import com.shlee.githubsearch.extension.cloneAndRemoveElement
import com.shlee.githubsearch.extension.with
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class SearchViewModel(
    private val repository: UserRepository
) : BaseViewModel() {

    private val userName = MutableLiveData<String>()
    private val result = map(userName) {
        repository.searchByName(it, true)
    }
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
            .subscribe {
                userName.value = it
            }
        )
    }

    fun onQueryChanged(query: CharSequence) {
        autoCompletePublishSubject.accept(query.toString())
    }

    fun retrieveAll() {
        addToDisposable(repository.retrieveAllBookmark().with()
            .map {
                it.map { bookmark ->  Bookmark.toUser(bookmark) }
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

        addToDisposable(repository.addToBookmark(user).with()
            .subscribe({
                if (it > 0) {
                    val value = bookmarkItems.value!!
                    val newValue = value.cloneAndAddElement(user)

                    bookmarkItems.value = newValue.toMutableList()

                    updatedBookmarkId.postValue(user.id)
                }
            }, {

            }))
    }

    fun deleteBookmark(user: User) {
        addToDisposable(repository.deleteBookmark(Bookmark.fromUser(user)).with()
            .subscribe({
                if (it > 0) {
                    val value = bookmarkItems.value!!
                    val deletedIndex = value.indexOfFirst { user.id == it.id }
                    val newValue = value.cloneAndRemoveElement(deletedIndex)

                    bookmarkItems.value = newValue.toMutableList()

                    updatedBookmarkId.postValue(user.id)
                }
            }, {

            })
        )
    }

    fun refresh() {
        result.value?.refresh?.invoke()
    }

}