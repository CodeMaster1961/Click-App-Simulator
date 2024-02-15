package com.example.clickingsimulator.ui.screens.achievements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.clickingsimulator.R
import com.example.clickingsimulator.ui.navigation.NavigationDestination

object AchievementDestination : NavigationDestination {
    override val route: String = "achievements"
    override val titleRes: Int = R.string.achievements_title
}

@Composable
fun HelloWorldText(
    achievementsViewModel: AchievementsViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.achievements_title))
    }
}