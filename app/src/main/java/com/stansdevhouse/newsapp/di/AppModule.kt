package com.stansdevhouse.newsapp.di

import android.content.Context
import androidx.room.Room
import com.stansdevhouse.newsapp.db.NewsDao
import com.stansdevhouse.newsapp.db.NewsDatabase
import com.stansdevhouse.newsapp.domain.NewsRepository
import com.stansdevhouse.newsapp.domain.NewsRepositoryDelegate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object AppModule {

    @ExperimentalCoroutinesApi
    @Provides
    @Singleton
    fun providesNewsRepository(newsRepository: NewsRepository): NewsRepositoryDelegate = newsRepository

    @Provides
    @Singleton
    fun providesNewsDatabase(@ApplicationContext appContext: Context): NewsDatabase =
        Room.databaseBuilder(
            appContext,
            NewsDatabase::class.java,
            "news_db"
        ).build()

    @Provides
    fun providesNewsDao(newsDatabase: NewsDatabase): NewsDao = newsDatabase.newsDao()

}