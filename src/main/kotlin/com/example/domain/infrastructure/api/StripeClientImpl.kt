package com.example.domain.infrastructure.api

import com.example.domain.entity.Content
import com.example.domain.infrastructure.dao.StripeClient
import io.ktor.serialization.*
import io.ktor.utils.io.errors.*
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import okhttp3.*

@Serializable
data class ListProductsResponse(
    val url: String,
    @SerialName("object") val obj: String,
    @SerialName("has_more") val hasMore: Boolean,
    val data: List<ContentDTO>,
)

@Serializable
data class ContentDTO(
    val id: String,
    @SerialName("object") val datumObject: String?,
    val name: String,
    val url: String? = null,
    val active: Boolean,
    val attributes: List<String?>,
    @SerialName("default_price") val defaultPrice: String?,
    val description: String? = null,
    val images: List<String>,
    val livemode: Boolean,
    val metadata: Map<String, String>,
    @SerialName("package_dimensions") val packageDimensions: String? = null,
    val shippable: Boolean? = null,
    @SerialName("statement_descriptor") val statementDescriptor: String? = null,
    @SerialName("tax_code") val taxCode: String? = null,
    val type: String,
    @SerialName("unit_label") val unitLabel: String? = null,
    val created: Long,
    val updated: Long,
)

@Serializable
data class ListPricesResponse(
    val url: String,
    @SerialName("object") val obj: String,
    @SerialName("has_more") val hasMore: Boolean,
    val data: List<PriceDTO>,
)

@Serializable
data class PriceDTO(
    @SerialName("product") val productId: String,
    @SerialName("unit_amount") val unitAmount: Int,
    val active: Boolean,
)

class StripeClientImpl: StripeClient {
    private val client = OkHttpClient()
    private val baseRequest =
        Request.Builder()
            .addHeader("Authorization", "Bearer ${System.getenv("STRIPE_SECRET_KEY")}")
            .addHeader("ContentType", "application/x-www-form-urlencoded")
    private val jsonDecoder = Json { ignoreUnknownKeys = true }

    private fun baseUrl(): HttpUrl.Builder {
        return HttpUrl.Builder().scheme("https").host("api.stripe.com").addPathSegment("v1")
    }

    override fun listAllProducts(): List<Content> {
        val listProductsRequest =
            baseRequest.url(baseUrl().addPathSegment("products").build()).get().build()
        val listProductsResponseBody = client.newCall(listProductsRequest).execute().body
        val contents =
            jsonDecoder
                .decodeFromString<ListProductsResponse>(listProductsResponseBody!!.string())
                .data

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

        val listPricesRequest =
            baseRequest
                .url(
                    baseUrl()
                        .addPathSegment("prices")
                        .addPathSegment("search")
                        .addEncodedQueryParameter("query", productQuery)
                        .build()
                )
                .get()
                .build()
        val listPricesResponseBody = client.newCall(listPricesRequest).execute().body
        println(listPricesResponseBody)
        val prices =
            jsonDecoder.decodeFromString<ListPricesResponse>(listPricesResponseBody!!.string()).data

        val priceMap: MutableMap<String, Int> = mutableMapOf()
        prices.forEach { price ->
            if (price.active) {
                priceMap[price.productId] = price.unitAmount
            }
        }
        return priceMap
    }
}
