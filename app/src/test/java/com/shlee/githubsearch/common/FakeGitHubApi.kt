package com.shlee.githubsearch.common

import com.shlee.githubsearch.data.network.GitHubApiService
import com.shlee.githubsearch.domain.SearchResponse
import com.shlee.githubsearch.domain.User
import retrofit2.Call
import retrofit2.mock.Calls

class FakeGitHubApi : GitHubApiService {

    var users = emptyList<User>()

    override fun searchUsersByPage(
        name: String,
        page: Int,
        perPage: Int
    ): Call<SearchResponse> {
        val searchResponse = SearchResponse(
            totalCount = users.size,
            incompleteResults = false,
            users = users
        )

        return Calls.response(searchResponse)
    }

    fun addUser(size: Int) {
        val userFactory = UserFactory()
        users = (0 until size).map { userFactory.createUser() }
    }

}