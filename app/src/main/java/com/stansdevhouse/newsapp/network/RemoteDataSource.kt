package com.stansdevhouse.newsapp.network

import com.stansdevhouse.newsapp.domain.RequestResult
import com.stansdevhouse.newsapp.network.response.NewsResponse
import com.stansdevhouse.newsapp.network.response.NewsResponse.Companion.toDomainModel
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val cbcApiServiceDelegate: CbcApiServiceDelegate) {

    suspend fun fetchAllNews(): RequestResult =
        getResponse(request = { cbcApiServiceDelegate.getNewsList() })

    private suspend fun getResponse(request: suspend () -> Response<List<NewsResponse>>): RequestResult {
        return try {
            RequestResult.Loading
            val result = request.invoke()
            if (result.isSuccessful) {
                RequestResult.Success(result.body()?.toDomainModel() ?: emptyList())
            } else {
                RequestResult.Error(result.code().toHttpCodeMessage())
            }
        } catch (e: Throwable) {
            RequestResult.Error(e.message ?: "Error fetching news")
        } catch (e: IOException) {
            RequestResult.Error(e.message ?: "Error fetching news")
        }
    }
}

fun Int.toHttpCodeMessage(): String {
    return when (this) {
        HttpURLConnection.HTTP_FORBIDDEN -> "Forbidden"
        HttpURLConnection.HTTP_BAD_GATEWAY -> "Bad Gateway"
        HttpURLConnection.HTTP_BAD_REQUEST -> "Bad Request"
        HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> "Gateway timeout"
        HttpURLConnection.HTTP_NOT_FOUND -> "Resource Not Found"
        else -> "Error fetching news"
    }
}