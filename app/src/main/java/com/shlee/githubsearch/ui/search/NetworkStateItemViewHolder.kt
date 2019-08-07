package com.shlee.githubsearch.ui.search

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.shlee.githubsearch.databinding.ItemNetworkStateBinding

class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding: ItemNetworkStateBinding = DataBindingUtil.bind(view)!!
}