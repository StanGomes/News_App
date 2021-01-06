package com.stansdevhouse.newsapp.network

import com.stansdevhouse.newsapp.network.response.NewsResponse
import retrofit2.Response
import javax.inject.Inject

class CbcApiService @Inject constructor(private val apiService: ApiService) : CbcApiServiceDelegate {
    override suspend fun getNewsList(): Response<List<NewsResponse>> = apiService.getNewsList()
}