package com.stansdevhouse.newsapp.network

import com.stansdevhouse.newsapp.network.model.NewsItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("aggregate_api/v1/items")
    suspend fun getNewsList(@Query("lineupSlug") lineupSlug: String = "news") : Response<List<NewsItem>>
}