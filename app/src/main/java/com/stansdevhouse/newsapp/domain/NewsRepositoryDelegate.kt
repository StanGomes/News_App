package com.stansdevhouse.newsapp.domain

import com.stansdevhouse.newsapp.domain.model.News
import com.stansdevhouse.newsapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepositoryDelegate {
    fun getAllNews() : Flow<Resource<List<News>>>
}