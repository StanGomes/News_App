package com.stansdevhouse.newsapp.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NewsItem(
        val description: String,
        val id: Int,
        val publishedAt: Long,
        val source: String,
        val sourceId: String,
        val title: String,
        val type: String,
        val typeAttributes: TypeAttributes,
        val updatedAt: Long,
)

@Serializable
data class TypeAttributes(
        val author: Author,
        val deck: String,
        val components: Components?,
        val imageLarge: String,
        val imageSmall: String,
        val show: String,
        val url: String,
)

@Serializable
data class Author(
        val display: String?,
        val image: String?,
        val name: String?
)

@Serializable
data class Components(
        val mainContent: Component?,
        val mainVisual: Component?,
)

@Serializable
data class Component(
        val description: String?,
        val id: Int,
        val publishedAt: Long,
        val source: String,
        val title: String?,
        val type: String,
        val typeAttributes: TypeAttributes,
        val updatedAt: Long,
)
