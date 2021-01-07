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
import javax.inject.Inject

@ExperimentalCoroutinesApi
class NewsRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val newsDao: NewsDao
) : NewsRepositoryDelegate {

    override fun getAllNews(): Flow<Resource<List<News>>> {
        return flow {
            getCachedNews().run {
                if (this.data.isNullOrEmpty().not()) {
                    emit(this)
                }
            }
            val networkResult = remoteDataSource.fetchAllNews()
            if (networkResult.status == Status.SUCCESS) {
                networkResult.data?.let {
                    newsDao.insertNews(it.toDbModel())
                }
                emit(getCachedNews())
            }
            if (networkResult.status == Status.ERROR) {
                getCachedNews().run {
                    if (this.data.isNullOrEmpty()) {
                        emit(networkResult)
                    } else emit(this)
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun getCachedNews(): Resource<List<News>> = Resource.success(newsDao.getAllNews().toDomainModel())

}