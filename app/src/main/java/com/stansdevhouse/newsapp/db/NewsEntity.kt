package com.stansdevhouse.newsapp.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "news_table")
data class NewsEntity(
    @PrimaryKey
    val id: Int,
    val description: String,
    val publishedAt: Long?,
    val source: String?,
    val sourceId: String?,
    val title: String?,
    val type: String?,
    val typeAttributes: DbTypeAttributes?,
    val updatedAt: Long?,
)

@Serializable
data class DbTypeAttributes(
    @Embedded
    val author: DbAuthor?,
    val deck: String?,
    @Embedded
    val components: DbComponents?,
    val imageLarge: String?,
    val imageSmall: String?,
    val show: String?,
    val url: String?,
)

@Serializable
data class DbAuthor(
    val display: String?,
    val image: String?,
    val name: String?
)

@Serializable
data class DbComponents(
    @Embedded
    val mainContent: DbComponent?,
    @Embedded
    val mainVisual: DbComponent?,
)

@Serializable
data class DbComponent(
    val componentDescription: String,
    val componentId: Int,
    val componentPublishedAt: Long?,
    val componentSource: String?,
    val componentTitle: String?,
    val componentType: String?,
    val componentTypeAttributes: DbTypeAttributes?,
    val componentUpdatedAt: Long?,
)
