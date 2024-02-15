package com.example.clickingsimulator

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.clickingsimulator.ui.screens.home.HomeViewModel
import org.junit.Rule
import org.junit.Test

class ClickSimulatorTest {

    @get:Rule
    val composableRule = createComposeRule()

    @Test
    fun testIncreaseProgressBarComposable() {
        val viewModel = HomeViewModel()

        composableRule.setContent {
            IncreaseProgressBar(viewModel = viewModel)
        }

        composableRule.onNodeWithText("Click").performClick()
    }
}