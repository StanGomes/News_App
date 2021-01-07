package com.stansdevhouse.newsapp.domain

import com.stansdevhouse.newsapp.domain.model.News

sealed class RequestResult {
    object Loading: RequestResult()
    data class Success(val news: List<News>) : RequestResult()
    data class Error(val errorMessage: String) : RequestResult()
}