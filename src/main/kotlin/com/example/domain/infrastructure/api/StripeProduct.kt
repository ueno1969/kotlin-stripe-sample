package com.example.domain.infrastructure.api

import com.example.domain.entity.Content
import com.example.domain.infrastructure.dao.StripeClient
import com.stripe.Stripe
import com.stripe.model.Price
import com.stripe.model.Product


class StripeProduct: StripeClient {
    override fun listAllProducts(): List<Content> {
        Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY")

        val productParams = mapOf("limit" to 100)
        val contents = Product.list(productParams).autoPagingIterable()
        val productIds = contents.map { it.id }
        val priceMap = listActivePriceMapByProductIDs(productIds)

        return contents.map { Content(id = it.id, name = it.name, price = priceMap[it.id]!!) }
    }

    private fun listActivePriceMapByProductIDs(productIds: List<String>): Map<String, Int> {
        var productQuery = ""
        productIds.forEachIndexed { i, id ->
            productQuery += "product:'${id}'"
            if (i < productIds.size - 1) {
                productQuery += " OR "
            }
        }

        val prices = Price.search(mapOf("query" to productQuery)).data
        val priceMap: MutableMap<String, Int> = mutableMapOf()
        prices.forEach { price ->
            if (price.active) {
                priceMap[price.product] = price.unitAmount.toInt()
            }
        }
        return priceMap
    }

}