package com.example.clickingsimulator.data.repository

import com.example.clickingsimulator.data.models.Achievement

interface AchievementRepository {
    suspend fun getAllAchievements(): List<Achievement>
}