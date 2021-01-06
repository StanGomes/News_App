package com.stansdevhouse.newsapp.network

import com.stansdevhouse.newsapp.network.response.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("aggregate_api/v1/items")
    suspend fun getNewsList(
        @Query("lineupSlug") lineupSlug: String = "news",
        @Query("page") page: Int = 1
    ) : Response<List<NewsResponse>>
}