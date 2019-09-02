package com.shlee.githubsearch.ui.bookmark

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.shlee.githubsearch.R
import com.shlee.githubsearch.databinding.FragmentBookmarkBinding
import com.shlee.githubsearch.ui.BindingFragment
import com.shlee.githubsearch.util.provideSearchViewModelFactory
import com.shlee.githubsearch.viewmodels.SearchViewModel

class BookmarkFragment : BindingFragment<FragmentBookmarkBinding>() {

    override fun getLayoutResId() = R.layout.fragment_bookmark

    private lateinit var viewModel: SearchViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity ?. let {
            viewModel =
                ViewModelProvider(it, provideSearchViewModelFactory(requireContext()))
                    .get(SearchViewModel::class.java)
            binding.viewModel = viewModel
            binding.lifecycleOwner = it

            if (viewModel.bookmarkItems.value.isNullOrEmpty()) {
                viewModel.retrieveAllBookmark()
            }
        }
    }

}