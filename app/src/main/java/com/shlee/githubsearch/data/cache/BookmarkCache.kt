package com.shlee.githubsearch.data.cache

import com.shlee.githubsearch.data.db.Bookmark
import com.shlee.githubsearch.domain.User

interface BookmarkCache {

    fun isBookmarked(user: User): Boolean

    fun saveBookmark(bookmark: Bookmark)

    fun saveBookmarks(bookmarks: List<Bookmark>)

    fun removeBookmark(bookmark: Bookmark)

}