package com.shlee.githubsearch.ui.search

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SearchView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shlee.githubsearch.R
import com.shlee.githubsearch.data.UserRepository
import com.shlee.githubsearch.data.network.LoadingState
import com.shlee.githubsearch.domain.User
import com.shlee.githubsearch.viewmodels.SearchViewModel
import java.lang.IllegalArgumentException

class SearchAdapter(
    var viewModel: SearchViewModel,
    var userRepository: UserRepository
) : PagedListAdapter<User, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var loadingState: LoadingState? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.item_network_state -> NetworkStateItemViewHolder(
                        LayoutInflater
                            .from(parent.context)
                            .inflate(R.layout.item_network_state, parent, false))
            R.layout.item_user -> SearchItemViewHolder(
                        LayoutInflater
                            .from(parent.context)
                            .inflate(R.layout.item_user, parent, false))
            else -> throw IllegalArgumentException()
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            for (payload in payloads) {
                if (payload is String) {
                    when (payload) {
                        PAYLOAD_UPDATE_BOOKMARK ->
                            updateBookmarkSelection(holder as SearchItemViewHolder, position)
                    }
                }
            }
        }
    }

    private fun updateBookmarkSelection(
        holder: SearchItemViewHolder,
        position: Int
    ) = getItem(position)?.run {
            holder.binding.imgLike.isSelected = userRepository.isBookmarked(this)
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)) {
            R.layout.item_network_state -> {
                (holder as NetworkStateItemViewHolder).binding.loadingState = loadingState
            }
            R.layout.item_user -> {
                getItem(position)?.run {
                    (holder as SearchItemViewHolder).binding.item = this
                    holder.binding.vm = viewModel
                    holder.binding.userRepository = userRepository
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (hasExtraRow() && (position == itemCount - 1)) {
            R.layout.item_network_state
        } else {
            R.layout.item_user
        }

    private fun hasExtraRow(): Boolean {
        return loadingState != null && loadingState != LoadingState.LOADED
    }

    fun setLoadingState(newLoadingState: LoadingState?) {
        val previousState = this.loadingState
        val hadExtraRow = hasExtraRow()
        this.loadingState = newLoadingState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
            }
        } else if (hasExtraRow && previousState != newLoadingState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        const val PAYLOAD_UPDATE_BOOKMARK = "update_bookmark"

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(
                oldItem: User, newItem: User
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: User,
                newItem: User
            ): Boolean = oldItem == newItem
        }
    }

}
