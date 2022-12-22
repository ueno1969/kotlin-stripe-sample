package com.example.domain.infrastructure.dao

import com.example.domain.entity.Content
import com.example.domain.infrastructure.api.StripeClient
import com.example.domain.repository.ContentRepository

class ContentRepositoryImpl(
    private val stripeClient: StripeClient,
) : ContentRepository {
    override fun listAll(): List<Content> {
        val contents = stripeClient.listAllProducts()
        return contents.map {
            Content(
                id = it.id,
                name = it.name,
                price = it.price,
            )
        }
//        return listOf(
//            Content(1, "赤ペン", 200),
//            Content(2, "青ペン", 200),
//            Content(3, "黄ペン", 300),
//        )
    }
}