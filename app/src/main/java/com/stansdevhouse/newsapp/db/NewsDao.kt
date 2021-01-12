package com.stansdevhouse.newsapp.db

import androidx.room.*

@Dao
interface NewsDao {

    @Query("SELECT * FROM news_table")
    fun getAllNews(): List<NewsEntity>

    @Query("SELECT DISTINCT type FROM news_table")
    fun getTypes(): List<String>

    @Query("SELECT * FROM news_table WHERE type LIKE :type")
    fun getNewsByType(type: String): List<NewsEntity>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(freshNews: List<NewsEntity>)

    @Transaction
    @Delete
    suspend fun deleteOldNews(newsEntity: NewsEntity)

    @Transaction
    suspend fun insertAndDeleteOldNews(freshNews: List<NewsEntity>, oldNews: List<NewsEntity>) {
        insertNews(freshNews)
        oldNews.forEach {
            deleteOldNews(it)
        }
    }
}