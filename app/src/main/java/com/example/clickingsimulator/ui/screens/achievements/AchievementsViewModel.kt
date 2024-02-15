package com.example.clickingsimulator.ui.screens.achievements

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickingsimulator.data.models.Achievement
import com.example.clickingsimulator.data.repository.AchievementRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface AchievementUiState {
    data class Success(val achievements: List<Achievement>) : AchievementUiState
    data object Loading : AchievementUiState
    data object Error : AchievementUiState
}

class AchievementsViewModel(
    private val achievementRepository: AchievementRepository
) : ViewModel() {

    private var achievementUiState: AchievementUiState by mutableStateOf(AchievementUiState.Loading)

    init {
        getAllAchievements()
    }

    private fun getAllAchievements() {
        viewModelScope.launch {
            achievementUiState = AchievementUiState.Loading
            achievementUiState = try {
                AchievementUiState.Success(achievementRepository.getAllAchievements())
            } catch (error: IOException) {
                AchievementUiState.Error
            } catch (error: HttpException) {
                AchievementUiState.Error
            }
        }
    }
}