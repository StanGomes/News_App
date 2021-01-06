package com.stansdevhouse.newsapp.network

import com.stansdevhouse.newsapp.domain.model.News
import com.stansdevhouse.newsapp.network.response.NewsResponse
import com.stansdevhouse.newsapp.network.response.NewsResponse.Companion.toDomainModel
import com.stansdevhouse.newsapp.util.Resource
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val cbcApiServiceDelegate: CbcApiServiceDelegate) {

    suspend fun fetchAllNews(): Resource<List<News>> = getResponse(request = { cbcApiServiceDelegate.getNewsList() })

    private suspend fun getResponse(request: suspend () -> Response<List<NewsResponse>>): Resource<List<News>> {
        return try {
            Resource.loading(null)
            val result = request.invoke()
            if (result.isSuccessful) {
                Resource.success(result.body()?.toDomainModel())
            } else {
                Resource.error("Error fetching news", null)
            }
        } catch (e: Throwable) {
            Resource.error(e.message ?: "Error fetching news", null)
        }
    }
}