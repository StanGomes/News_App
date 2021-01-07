package com.stansdevhouse.newsapp.domain

import com.stansdevhouse.newsapp.domain.model.News
import kotlinx.coroutines.flow.Flow

interface NewsRepositoryDelegate {
    fun getAllNews() : Flow<List<News>>
    fun getAllTypes() : Flow<List<String>>
    fun getNewsByType(type: String) : Flow<List<News>>
    suspend fun refresh(): Flow<RequestResult>
}