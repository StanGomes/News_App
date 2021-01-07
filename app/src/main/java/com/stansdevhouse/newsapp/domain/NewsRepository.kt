package com.stansdevhouse.newsapp.domain

import com.stansdevhouse.newsapp.db.NewsDao
import com.stansdevhouse.newsapp.db.toDbModel
import com.stansdevhouse.newsapp.db.toDomainModel
import com.stansdevhouse.newsapp.domain.model.News
import com.stansdevhouse.newsapp.network.RemoteDataSource
import com.stansdevhouse.newsapp.util.Resource
import com.stansdevhouse.newsapp.util.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class NewsRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val newsDao: NewsDao
) : NewsRepositoryDelegate {

    private val simpleDateFormat = SimpleDateFormat(
        "EEE, d MMM yyyy h:mm a",
        Locale.getDefault()
    )

    override fun getAllNews(): Flow<Resource<List<News>>> = flow {
        emit(getCachedNews())
    }.flowOn(Dispatchers.IO)

    override fun getAllTypes(): Flow<List<String>> = flow {
        emit(newsDao.getTypes().map { it.capitalize(Locale.getDefault()) })
    }.flowOn(Dispatchers.IO)

    override fun getNewsByType(type: String): Flow<List<News>> = flow {
        emit(newsDao.getNewsByType(type).toDomainModel(simpleDateFormat))
    }.flowOn(Dispatchers.IO)

    override suspend fun refresh() {
        val networkResult = remoteDataSource.fetchAllNews()
        if (networkResult.status == Status.SUCCESS) {
            networkResult.data?.let {
                newsDao.insertNews(it.toDbModel())
            }
        }
    }

    private fun getCachedNews(): Resource<List<News>> {
        return Resource.success(
            newsDao.getAllNews().toDomainModel(simpleDateFormat)
        )
    }

}