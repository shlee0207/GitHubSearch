package com.shlee.githubsearch.ui.search

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.shlee.githubsearch.R
import com.shlee.githubsearch.data.network.LoadingState
import com.shlee.githubsearch.databinding.FragmentSearchBinding
import com.shlee.githubsearch.domain.User
import com.shlee.githubsearch.ui.BindingFragment
import com.shlee.githubsearch.util.provideSearchViewModelFactory
import com.shlee.githubsearch.util.provideUserRepository
import com.shlee.githubsearch.viewmodels.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BindingFragment<FragmentSearchBinding>() {

    private lateinit var viewModel: SearchViewModel

    override fun getLayoutResId() = R.layout.fragment_search

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity ?. let {
            viewModel = ViewModelProvider(it, provideSearchViewModelFactory(requireContext()))
                .get(SearchViewModel::class.java)

            binding.viewModel = viewModel
            binding.userRepository = provideUserRepository(requireContext())
            binding.lifecycleOwner = it
            /*binding.recycleView.addItemDecoration(
                DividerItemDecoration(recycleView.context, DividerItemDecoration.VERTICAL)
            )*/
            initSwipeRefresh()
        }

    }

    private fun initSwipeRefresh() {
        viewModel.refreshState.observe(activity as FragmentActivity,
            Observer { loadingState ->
                binding.swipeRefresh.isRefreshing = (loadingState == LoadingState.LOADING)
            }
        )
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

}