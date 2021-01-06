package com.stansdevhouse.newsapp.network.response

import com.stansdevhouse.newsapp.domain.model.*
import com.stansdevhouse.newsapp.network.response.AuthorResponse.Companion.toDomainModel
import com.stansdevhouse.newsapp.network.response.ComponentResponse.Companion.toDomainModel
import com.stansdevhouse.newsapp.network.response.ComponentsResponse.Companion.toDomainModel
import com.stansdevhouse.newsapp.network.response.TypeAttributesResponse.Companion.toDomainModel
import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val description: String,
    val id: Int,
    val publishedAt: Long? = null,
    val source: String? = null,
    val sourceId: String? = null,
    val title: String? = null,
    val type: String? = null,
    val typeAttributes: TypeAttributesResponse? = null,
    val updatedAt: Long? = null,
) {
    companion object {
        fun List<NewsResponse>.toDomainModel(): List<News> =
            this.map {
                News(
                    it.description,
                    it.id,
                    it.publishedAt,
                    it.source,
                    it.sourceId,
                    it.title,
                    it.type,
                    it.typeAttributes?.toDomainModel(),
                    it.updatedAt
                )
            }
    }
}

@Serializable
data class TypeAttributesResponse(
    val author: AuthorResponse? = null,
    val deck: String? = null,
    val components: ComponentsResponse? = null,
    val imageLarge: String? = null,
    val imageSmall: String? = null,
    val show: String? = null,
    val url: String? = null,
) {
    companion object {
        fun TypeAttributesResponse.toDomainModel(): TypeAttributes =
            TypeAttributes(
                author?.toDomainModel(),
                deck,
                components?.toDomainModel(),
                imageLarge,
                imageSmall,
                show,
                url
            )
    }
}

@Serializable
data class AuthorResponse(
    val display: String? = null,
    val image: String? = null,
    val name: String? = null
) {
    companion object {
        fun AuthorResponse.toDomainModel(): Author =
            Author(display, image, name)
    }
}

@Serializable
data class ComponentsResponse(
    val mainContent: ComponentResponse? = null,
    val mainVisual: ComponentResponse? = null,
) {
    companion object {
        fun ComponentsResponse.toDomainModel(): Components =
            Components(
                mainContent?.toDomainModel(),
                mainVisual?.toDomainModel()
            )
    }
}

@Serializable
data class ComponentResponse(
    val description: String,
    val id: Int,
    val publishedAt: Long? = null,
    val source: String? = null,
    val title: String? = null,
    val type: String? = null,
    val typeAttributes: TypeAttributesResponse? = null,
    val updatedAt: Long? = null,
) {
    companion object {
        fun ComponentResponse.toDomainModel(): Component =
            Component(
                description,
                id,
                publishedAt,
                source,
                title,
                type,
                typeAttributes?.toDomainModel(),
                updatedAt
            )
    }
}
