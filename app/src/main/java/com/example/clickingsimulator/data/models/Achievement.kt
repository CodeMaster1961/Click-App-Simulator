package com.example.clickingsimulator.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Achievement(
    val achievementId: Int,
    val achievementDescription: String,
    val achievementImage: String
)
