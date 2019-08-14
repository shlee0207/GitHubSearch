package com.shlee.githubsearch.ui.bookmark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shlee.githubsearch.R
import com.shlee.githubsearch.databinding.ItemBookmarkBinding
import com.shlee.githubsearch.domain.User
import com.shlee.githubsearch.viewmodels.SearchViewModel

class BookmarkAdapter(
    var items: List<User>,
    var viewModel: SearchViewModel
) : ListAdapter<User, BookmarkAdapter.BookmarkViewHolder> (ItemDiffUtil()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookmarkViewHolder =
        BookmarkViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_bookmark, parent, false)
        )

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.binding.item = items[position]
        holder.binding.vm = viewModel
    }

    class BookmarkViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemBookmarkBinding = DataBindingUtil.bind(view)!!
    }

    class ItemDiffUtil : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

}