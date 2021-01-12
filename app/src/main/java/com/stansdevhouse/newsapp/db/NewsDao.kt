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
    @Query("DELETE FROM news_table WHERE id NOT IN(:idList)")
    suspend fun deleteOldNews(idList: List<Int>)

    @Transaction
    suspend fun insertAndDeleteOldNews(freshNews: List<NewsEntity>, idList: List<Int>) {
        insertNews(freshNews)
        deleteOldNews(idList)
    }
}