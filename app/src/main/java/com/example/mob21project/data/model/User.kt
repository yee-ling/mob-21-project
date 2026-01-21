package com.example.mob21project.data.model

data class User (
    val id: String = "",
    val fullName: String = "",
    val email: String = "",
    val role: Role = Role.USER,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class Role {
    USER, ADMIN
}