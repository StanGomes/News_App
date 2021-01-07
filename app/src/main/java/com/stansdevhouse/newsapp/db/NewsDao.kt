package com.stansdevhouse.newsapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDao {

    @Query("SELECT * FROM news_table")
    fun getAllNews(): List<NewsEntity>

    @Query("SELECT DISTINCT type FROM news_table")
    fun getTypes(): List<String>

    @Query("SELECT * FROM news_table WHERE type LIKE :type")
    fun getNewsByType(type: String): List<NewsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(allNews: List<NewsEntity>)
}