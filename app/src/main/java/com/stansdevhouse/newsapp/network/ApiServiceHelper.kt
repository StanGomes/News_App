package com.stansdevhouse.newsapp.network

import com.stansdevhouse.newsapp.network.model.NewsItem
import retrofit2.Response

interface ApiServiceHelper {
    suspend fun getNewsList(): Response<List<NewsItem>>
}