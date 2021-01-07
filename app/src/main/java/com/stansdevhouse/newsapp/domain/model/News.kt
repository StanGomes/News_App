package com.stansdevhouse.newsapp.domain.model

data class News(
    val description: String,
    val id: Int,
    val publishedAt: Long?,
    val source: String?,
    val sourceId: String?,
    val title: String?,
    val type: String?,
    val typeAttributes: TypeAttributes?,
    val updatedAt: Long?,
    val readablePublishedAt: String? = null
)

data class TypeAttributes(
    val author: Author?,
    val deck: String?,
    val components: Components?,
    val imageLarge: String?,
    val imageSmall: String?,
    val show: String?,
    val url: String?,
)

data class Author(
    val display: String?,
    val image: String?,
    val name: String?
)

data class Components(
    val mainContent: Component?,
    val mainVisual: Component?,
)

data class Component(
    val description: String,
    val id: Int,
    val publishedAt: Long?,
    val source: String?,
    val title: String?,
    val type: String?,
    val typeAttributes: TypeAttributes?,
    val updatedAt: Long?,
)


