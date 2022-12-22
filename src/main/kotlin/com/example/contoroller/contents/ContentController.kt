package com.example.contoroller.contents

import com.example.domain.entity.Content
import com.example.usecase.ListContentUseCase

data class ContentView(
    val id: Int,
    val name: String,
    val price: Int
) {
    companion object {
        fun of(content: Content): ContentView {
            return ContentView(
                id = content.id,
                name = content.name,
                price = content.price,
            )
        }
    }
}

data class ContentsView(
    val values: List<ContentView>,
) {
    companion object {
        fun of(contents: List<Content>): ContentsView {
            return ContentsView(
                values = contents.map { ContentView.of(it) }
            )
        }
    }
}

class ContentController(
    private val listContentUseCase: ListContentUseCase
) {
    fun list(): ContentsView {
        return ContentsView.of(listContentUseCase.execute())
    }
}
