package com.shlee.githubsearch.data.cache

import com.shlee.githubsearch.data.db.Bookmark
import com.shlee.githubsearch.domain.User

class BookmarkCacheImpl : BookmarkCache{

    private val cache = hashSetOf<Long>()

    override fun isBookmarked(user: User): Boolean {
        return cache.contains(user.id)
    }

    override fun saveBookmark(bookmark: Bookmark) {
        cache.add(bookmark.uid)
    }

    override fun saveBookmarks(bookmarks: List<Bookmark>) {
        for (bookmark in bookmarks) {
            cache.add(bookmark.uid)
        }
    }

    override fun removeBookmark(bookmark: Bookmark) {
        cache.remove(bookmark.uid)
    }
}