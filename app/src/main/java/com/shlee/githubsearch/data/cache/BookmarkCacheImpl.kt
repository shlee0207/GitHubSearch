package com.shlee.githubsearch.data.cache

import com.shlee.githubsearch.data.db.Bookmark
import com.shlee.githubsearch.domain.User

class BookmarkCacheImpl : BookmarkCache{

    private val cache = hashSetOf<Long>()

    override fun isBookmarked(user: User): Boolean {
        return cache.contains(user.id)
    }

    override fun saveToCache(bookmark: Bookmark) {
        cache.add(bookmark.uid)
    }

    override fun removeBookmark(bookmark: Bookmark) {
        cache.remove(bookmark.uid)
    }
}