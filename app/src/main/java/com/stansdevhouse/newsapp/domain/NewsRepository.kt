package com.stansdevhouse.newsapp.domain

import com.stansdevhouse.newsapp.domain.model.News
import com.stansdevhouse.newsapp.network.RemoteDataSource
import com.stansdevhouse.newsapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ExperimentalCoroutinesApi
class NewsRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) : NewsRepositoryDelegate {

    override fun getAllNews(): Flow<Resource<List<News>>> {
        return flow {
            val result = remoteDataSource.fetchAllNews()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

}