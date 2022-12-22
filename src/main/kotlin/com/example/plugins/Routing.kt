package com.example.plugins

import com.example.contoroller.contents.ContentController
import com.example.contoroller.contents.ContentsView
import com.example.domain.infrastructure.api.StripeClientImpl
import com.example.domain.infrastructure.api.StripeProduct
import com.example.domain.infrastructure.dao.ContentRepositoryImpl
import com.example.usecase.ListContentUseCase
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*

fun Application.configureRouting() {
    routing {
        get("/") {
//            val stripeClient = StripeClientImpl()
            val stripeClient = StripeProduct()
            val contentRepository = ContentRepositoryImpl(stripeClient)
            val listContentUseCase = ListContentUseCase(contentRepository)
            val contentController = ContentController(
                listContentUseCase = listContentUseCase
            )
            val contents: ContentsView = contentController.list()
            call.respond(FreeMarkerContent("contents.ftl", mapOf("contents" to contents.values)))
        }
    }
}