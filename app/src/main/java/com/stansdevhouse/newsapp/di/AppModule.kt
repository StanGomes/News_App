package com.stansdevhouse.newsapp.di

import com.stansdevhouse.newsapp.domain.NewsRepository
import com.stansdevhouse.newsapp.domain.NewsRepositoryDelegate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object AppModule {

    @ExperimentalCoroutinesApi
    @Provides
    @Singleton
    fun providesNewsRepository(newsRepository: NewsRepository): NewsRepositoryDelegate = newsRepository

}