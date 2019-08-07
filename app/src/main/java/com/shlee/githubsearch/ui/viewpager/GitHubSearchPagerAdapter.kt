package com.shlee.githubsearch.ui.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.shlee.githubsearch.ui.bookmark.BookmarkFragment
import com.shlee.githubsearch.ui.search.SearchFragment
import java.lang.IllegalArgumentException

class GitHubSearchPagerAdapter(
    fm: FragmentManager,
    behavior: Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) : FragmentPagerAdapter(fm, behavior) {

    companion object {
        const val PAGE_COUNT = 2
        const val FRAGMENT_SEARCH = 0
        const val FRAGMENT_BOOKMARK = 1
    }

    override fun getItem(position: Int): Fragment =
        when (position) {
            FRAGMENT_SEARCH -> SearchFragment()
            FRAGMENT_BOOKMARK -> BookmarkFragment()
            else -> throw IllegalArgumentException()
        }

    override fun getCount() = PAGE_COUNT

}