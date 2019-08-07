package com.shlee.githubsearch.ui.search

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.shlee.githubsearch.databinding.ItemUserBinding


class SearchItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding: ItemUserBinding = DataBindingUtil.bind(view)!!
}