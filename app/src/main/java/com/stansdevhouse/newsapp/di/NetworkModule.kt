package com.stansdevhouse.newsapp.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.stansdevhouse.newsapp.network.ApiService
import com.stansdevhouse.newsapp.network.ApiServiceHelper
import com.stansdevhouse.newsapp.network.ApiServiceHelperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@ExperimentalSerializationApi
@InstallIn(ApplicationComponent::class)
@Module
object NetworkModule {

    private const val BASE_URL = "https://www.cbc.ca"
    private val contentType = MediaType.get("application/json")

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()

    @Provides
    @Singleton
    fun providesApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun providesApiServiceHelper(apiServiceHelperImpl: ApiServiceHelperImpl): ApiServiceHelper =
        apiServiceHelperImpl
}