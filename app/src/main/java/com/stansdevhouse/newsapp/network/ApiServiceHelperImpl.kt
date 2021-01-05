package com.stansdevhouse.newsapp.network

import com.stansdevhouse.newsapp.network.model.NewsItem
import retrofit2.Response
import javax.inject.Inject

class ApiServiceHelperImpl @Inject constructor(private val apiService: ApiService) : ApiServiceHelper {
    override suspend fun getNewsList(): Response<List<NewsItem>> = apiService.getNewsList()
}