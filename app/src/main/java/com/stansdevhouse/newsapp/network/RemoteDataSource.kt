package com.stansdevhouse.newsapp.network

import com.stansdevhouse.newsapp.domain.RequestResult
import com.stansdevhouse.newsapp.network.response.NewsResponse
import com.stansdevhouse.newsapp.network.response.NewsResponse.Companion.toDomainModel
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val cbcApiServiceDelegate: CbcApiServiceDelegate) {

    suspend fun fetchAllNews(): RequestResult = getResponse(request = { cbcApiServiceDelegate.getNewsList() })

    private suspend fun getResponse(request: suspend () -> Response<List<NewsResponse>>): RequestResult {
        return try {
            RequestResult.Loading
            val result = request.invoke()
            if (result.isSuccessful) {
                RequestResult.Success(result.body()?.toDomainModel() ?: emptyList())
            } else {
                RequestResult.Error("Error fetching news")
            }
        } catch (e: Throwable) {
            RequestResult.Error(e.message ?: "Error fetching news")
        }
    }
}