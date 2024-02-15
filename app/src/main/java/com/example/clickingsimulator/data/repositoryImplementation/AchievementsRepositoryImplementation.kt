package com.example.clickingsimulator.data.repositoryImplementation

import com.example.clickingsimulator.data.ApiService
import com.example.clickingsimulator.data.models.Achievement
import com.example.clickingsimulator.data.repository.AchievementRepository

class AchievementsRepositoryImplementation(
    private val api: ApiService
) : AchievementRepository {

    override suspend fun getAllAchievements(): List<Achievement> {
        return api.getAllAchievements()
    }
}