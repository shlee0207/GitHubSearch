package com.shlee.githubsearch.ui.bindingadapter

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shlee.githubsearch.R
import com.shlee.githubsearch.data.UserRepository
import com.shlee.githubsearch.data.network.LoadingState
import com.shlee.githubsearch.domain.User
import com.shlee.githubsearch.ui.bookmark.BookmarkAdapter
import com.shlee.githubsearch.ui.search.SearchAdapter
import com.shlee.githubsearch.ui.search.SearchAdapter.Companion.PAYLOAD_UPDATE_BOOKMARK
import com.shlee.githubsearch.viewmodels.SearchViewModel

@BindingAdapter(value = ["viewModel", "repositories", "userRepo"])
fun bindRepository(
    recyclerView: RecyclerView,
    viewModel: SearchViewModel,
    items: PagedList<User>?,
    userRepository: UserRepository
) {
    recyclerView.adapter?.run {
        if (this is SearchAdapter) {
            this.submitList(items)
        }
    } ?: run {
        SearchAdapter(viewModel, userRepository).apply {
            recyclerView.adapter = this
            this.submitList(items)
        }
    }
}

@BindingAdapter("loadingState")
fun bindLoadingState(
    recyclerView: RecyclerView,
    loadingState: LoadingState?
) {
    recyclerView.adapter?.run {
        if (this is SearchAdapter) {
            this.setLoadingState(loadingState)
        }
    }
}

@BindingAdapter("bookmarkSelection")
fun bindBookmarkSelection(
    recyclerView: RecyclerView,
    position: Int
) {
    recyclerView.adapter?.notifyItemChanged(position, PAYLOAD_UPDATE_BOOKMARK)
}

@BindingAdapter(value = ["viewModel", "bookmarkRepo"])
fun bindBookmarkRepository(
    recyclerView: RecyclerView,
    viewModel: SearchViewModel,
    items: List<User>
) {
    recyclerView.adapter ?. run {
        if (this is BookmarkAdapter) {
            this.items = items
            this.notifyDataSetChanged()
        }
    } ?: run {
        BookmarkAdapter(items, viewModel).apply {
            recyclerView.adapter = this
        }
    }
}

@BindingAdapter("bind_img")
fun bindImage(
    view: AppCompatImageView,
    url: String
) {
    Glide.with(view.context)
        .load(url)
        .placeholder(R.mipmap.ic_launcher)
        .thumbnail(0.1f)
        .apply(RequestOptions.circleCropTransform())
        .into(view)
}

@BindingAdapter("selected")
fun bindSelected(
    view: AppCompatImageView,
    selected: Boolean
) {
    view.isSelected = selected
}