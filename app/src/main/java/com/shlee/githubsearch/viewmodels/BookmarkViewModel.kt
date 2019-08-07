package com.shlee.githubsearch.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.shlee.githubsearch.data.UserRepository
import com.shlee.githubsearch.data.db.Bookmark
import com.shlee.githubsearch.domain.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class BookmarkViewModel(
    private val userRepository: UserRepository
) : BaseViewModel() {

    var items: MutableLiveData<MutableList<User>> = MutableLiveData<MutableList<User>>().apply { value = arrayListOf() }

    fun retrieveAll() {
        addToDisposable(userRepository.retrieveAllBookmark()
            .map {
                it.map { bookmark ->  Bookmark.toUser(bookmark) }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                items.value = it.toMutableList()
            }, {
                Log.d("lsh",it.message)
            })
        )
    }

    fun deleteBookmark(user: User) {
        addToDisposable(userRepository.deleteBookmark(Bookmark.fromUser(user))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it > 0) {
                    val index = items.value!!.indexOfFirst { user.id == it.id }
                    var value = items.value
                    value!!.removeAt(index)
                    items.value = value
                }
            }, {
                Log.d("lsh",it.message)
            })
        )

    }

    fun isBookmarkEmpty(): Boolean {
        return items.value.isNullOrEmpty()
    }

}