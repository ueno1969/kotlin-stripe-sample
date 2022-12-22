package com.example.usecase

import com.example.domain.entity.Content
import com.example.domain.repository.ContentRepository

class ListContentUseCase(
    private val contentRepository: ContentRepository
) {
    fun execute(): List<Content> {
        return contentRepository.listAll()
    }
}
