package com.example.usecase

import com.example.domain.entity.Content

class ListContentUseCase {
    fun execute(): List<Content> {
        return listOf(
            Content(1, "赤ペン", 100),
            Content(2, "青ペン", 200),
            Content(3, "黄ペン", 300),
        )
    }
}
