package com.shlee.githubsearch.util

import android.content.Context
import com.shlee.githubsearch.data.UserRepository
import com.shlee.githubsearch.data.UserRepositoryImpl
import com.shlee.githubsearch.data.cache.BookmarkCache
import com.shlee.githubsearch.data.cache.BookmarkCacheImpl
import com.shlee.githubsearch.data.db.AppDataBase
import com.shlee.githubsearch.data.db.BookmarkDao
import com.shlee.githubsearch.data.network.GitHubApiService
import com.shlee.githubsearch.data.network.GitHubApiServiceFactory
import com.shlee.githubsearch.viewmodels.SearchViewModelFactory

fun provideSearchViewModelFactory(context: Context): SearchViewModelFactory {
    val repository = provideUserRepository(context)
    return SearchViewModelFactory(repository)
}

fun provideUserRepository(context: Context): UserRepository {
    return UserRepositoryImpl.getInstance(/*context*/
        provideBookmarkDao(context),
        provideGitHubApiService(),
        provideBookmarkCache()
    )
}

fun provideBookmarkDao(context: Context): BookmarkDao {
    return AppDataBase.getInstance(context).bookmarkDao()
}

fun provideGitHubApiService(): GitHubApiService {
    return GitHubApiServiceFactory.makeGitHubApiService()
}

fun provideBookmarkCache(): BookmarkCache {
    return BookmarkCacheImpl()
}