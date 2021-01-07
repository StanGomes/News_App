package com.stansdevhouse.newsapp.db

import com.stansdevhouse.newsapp.domain.model.*

fun List<NewsEntity>.toDomainModel(): List<News> =
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

private fun DbTypeAttributes.toDomainModel(): TypeAttributes =
    TypeAttributes(
        author?.toDomainModel(),
        deck,
        components?.toDomainModel(),
        imageLarge,
        imageSmall,
        show,
        url
    )

private fun DbAuthor.toDomainModel(): Author =
    Author(display, image, name)

private fun DbComponents.toDomainModel(): Components =
    Components(
        mainContent?.toDomainModel(),
        mainVisual?.toDomainModel()
    )

private fun DbComponent.toDomainModel(): Component =
    Component(
        componentDescription,
       componentId,
        componentPublishedAt,
        componentSource,
        componentTitle,
        componentType,
        componentTypeAttributes?.toDomainModel(),
        componentUpdatedAt
    )

fun List<News>.toDbModel(): List<NewsEntity> =
    this.map {
        NewsEntity(
            it.id,
            it.description,
            it.publishedAt,
            it.source,
            it.sourceId,
            it.title,
            it.type,
            it.typeAttributes?.toDbModel(),
            it.updatedAt
        )
    }

private fun TypeAttributes.toDbModel(): DbTypeAttributes =
    DbTypeAttributes(
        author?.toDbModel(),
        deck,
        components?.toDbModel(),
        imageLarge,
        imageSmall,
        show,
        url
    )

private fun Author.toDbModel(): DbAuthor =
    DbAuthor(display, image, name)

private fun Components.toDbModel(): DbComponents =
    DbComponents(
        mainContent?.toDbModel(),
        mainVisual?.toDbModel()
    )

private fun Component.toDbModel(): DbComponent =
    DbComponent(
        description,
        id,
        publishedAt,
        source,
        title,
        type,
        typeAttributes?.toDbModel(),
        updatedAt
    )

