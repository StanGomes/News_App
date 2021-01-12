package com.stansdevhouse.newsapp.domain

import com.stansdevhouse.newsapp.db.NewsDao
import com.stansdevhouse.newsapp.db.toDbModel
import com.stansdevhouse.newsapp.db.toDomainModel
import com.stansdevhouse.newsapp.domain.model.News
import com.stansdevhouse.newsapp.network.RemoteDataSource
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

    private val simpleDateFormat by lazy {
        SimpleDateFormat(
            "EEE, d MMM yyyy h:mm a",
            Locale.getDefault()
        )
    }

    override fun getAllNews(): Flow<List<News>> = flow {
        emit(getCachedNews())
    }.flowOn(Dispatchers.IO)

    override fun getAllTypes(): Flow<List<String>> = flow {
        emit(newsDao.getTypes().map { it.capitalize(Locale.getDefault()) })
    }.flowOn(Dispatchers.IO)

    override fun getNewsByType(type: String): Flow<List<News>> = flow {
        emit(newsDao.getNewsByType(type).toDomainModel(simpleDateFormat))
    }.flowOn(Dispatchers.IO)

    override suspend fun refresh(): Flow<RequestResult> = flow {
        when (val networkResult = remoteDataSource.fetchAllNews()) {
            is RequestResult.Success -> {
                with(networkResult.news) {
                    newsDao.insertAndDeleteOldNews(this.toDbModel(), this.map { it.id })
                }
                emit(RequestResult.Success(getCachedNews()))
            }
            is RequestResult.Error -> emit(RequestResult.Error(networkResult.errorMessage))
        }
    }.flowOn(Dispatchers.IO)

    private fun getCachedNews(): List<News> = newsDao.getAllNews().toDomainModel(simpleDateFormat)

}