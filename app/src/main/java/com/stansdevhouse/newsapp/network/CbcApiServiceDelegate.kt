package com.stansdevhouse.newsapp.network

import com.stansdevhouse.newsapp.network.response.NewsResponse
import retrofit2.Response

interface CbcApiServiceDelegate {
    suspend fun getNewsList(): Response<List<NewsResponse>>
}