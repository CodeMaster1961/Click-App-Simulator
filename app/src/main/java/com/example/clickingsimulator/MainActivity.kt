package com.example.clickingsimulator


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.clickingsimulator.ui.screens.home.HomeViewModel
import com.example.clickingsimulator.ui.theme.ClickingSimulatorTheme
import com.example.clickingsimulator.ui.screens.home.ClickingSimulatorApp
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClickingSimulatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val homeViewModel: HomeViewModel by viewModel<HomeViewModel>()
                    ClickingSimulatorApp(homeViewModel = homeViewModel)
                }
            }
        }
    }
}

