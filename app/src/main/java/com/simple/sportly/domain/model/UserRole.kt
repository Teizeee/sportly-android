package com.simple.sportly.domain.model

enum class UserRole {
    CLIENT,
    TRAINER;

    companion object {
        fun fromApi(value: String): UserRole = when (value.uppercase()) {
            CLIENT.name -> CLIENT
            TRAINER.name -> TRAINER
            else -> throw IllegalStateException("Unsupported role: $value")
        }
    }
}
