package com.example.domain.repository

import com.example.domain.entity.Content

interface ContentRepository {
    fun listAll(): List<Content>
}
