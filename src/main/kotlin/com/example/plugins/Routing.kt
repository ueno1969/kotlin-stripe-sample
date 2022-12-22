package com.example.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*

data class Content(val id: Int, val name: String, val price: Int)

fun Application.configureRouting() {
    routing {
        get("/") {

            val contents: List<Content> = listOf(
                Content(1, "赤ペン", 100),
                Content(2, "青ペン", 200),
            )
            call.respond(FreeMarkerContent("contents.ftl", mapOf("contents" to contents)))
//            call.respondText("Hello World2!")
        }
    }
}