package com.example.clickingsimulator.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clickingsimulator.R
import com.example.clickingsimulator.data.models.ShopItem
import com.example.clickingsimulator.ui.navigation.NavigationDestination

object HomeDestination : NavigationDestination {
    override val route: String = "home"
    override val titleRes: Int = R.string.app_name
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClickingSimulatorApp(
    homeViewModel: HomeViewModel
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            ProgressDemo(viewModel = homeViewModel)
            HomeScreen(
                shopItemUiState = homeViewModel.shopItemUiState,
                onClick = { shopItem ->
                    homeViewModel.buyHandItem(shopItem)
                },
                onPassiveClick = {
                    homeViewModel.clickPerSecond()
                },
                shopItem = ShopItem(0,"",0,0)
            )
        }
    }
}

@Composable
fun ProgressDemo(
    viewModel: HomeViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp),
        verticalArrangement = Arrangement.Top
    ) {
        CustomProgressBar(progress = viewModel.progress)

        Spacer(modifier = Modifier.size(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Level ${viewModel.level}")
            Spacer(modifier = Modifier.padding(bottom = 15.dp))
            Text(
                text = viewModel.formatClickCount(), modifier = Modifier.padding(end = 200.dp)
            )
        }
        Spacer(modifier = Modifier.padding(bottom = 15.dp))
    }
    IncreaseProgressBar(viewModel = viewModel)
}

@Composable
fun HomeScreen(
    shopItemUiState: ShopItemUiState,
    onClick: (ShopItem) -> Unit,
    onPassiveClick: (ShopItem) -> Unit,
    shopItem: ShopItem,
    modifier: Modifier = Modifier
) {
    when (shopItemUiState) {
        is ShopItemUiState.Success -> HomeBody(
            shopItemList = shopItemUiState.shopItems,
            onClick = onClick,
            onPassiveClick = onPassiveClick,
            shopItem = shopItem
        )

        else -> {}
    }
}

@Composable
fun HomeBody(
    shopItemList: List<ShopItem>,
    onClick: (ShopItem) -> Unit,
    onPassiveClick: (ShopItem) -> Unit,
    shopItem: ShopItem,
    viewModel: HomeViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 120.dp)
    ) {
        if (shopItemList.isEmpty()) {
            Text(text = "There are no Shop Items available")
        } else {
            ListOfShopItems(
                shopItems = shopItemList,
                onClick = onClick,
                onPassiveClick
            )
        }
        Text(text = "Total CPS: ${viewModel.calculateClickPerSecond()}")
    }
}

@Composable
fun IncreaseProgressBar(viewModel: HomeViewModel) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                viewModel.updateProgress()
            },
            modifier = Modifier
                .padding(bottom = 15.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Click", modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}

@Composable
fun ListOfShopItems(
    shopItems: List<ShopItem>,
    onClick: (ShopItem) -> Unit,
    onPassiveClick: (ShopItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(columns = GridCells.Fixed(1)) {
        items(shopItems) { shopItem ->
            ClickableItemCard(
                shopItem = shopItem,
                onClick = { onClick(shopItem) },
                onPassiveClick = { onPassiveClick(shopItem) })
        }
    }
}

@Composable
fun ClickableItemCard(
    shopItem: ShopItem,
    onClick: (ShopItem) -> Unit,
    onPassiveClick: (ShopItem) -> Unit,
    viewModel: HomeViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        modifier = Modifier
            .size(380.dp, 100.dp)
            .padding(start = 7.dp, bottom = 10.dp)
            .clickable {
                onClick(shopItem)
                onPassiveClick(shopItem)
            }
    ) {
        Text(text = shopItem.shopItemName)
        Text(
            text = "Cost: ${viewModel.formatShopItemPrice(shopItem)}"
        )
        Text(
            text = "CPS: ${viewModel.formatShopItemClickPerSecond(shopItem)}"
        )
    }
}

@Composable
fun CustomProgressBar(progress: Float) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
            .background(Color.Black),
        onDraw = {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val progressBarWidth = canvasWidth * progress


            drawRoundRect(
                color = Color.Gray,
                size = size.copy(height = canvasHeight),
                cornerRadius = CornerRadius.Zero
            )


            drawRoundRect(
                color = Color.Red,
                size = size.copy(
                    width = progressBarWidth,
                    height = canvasHeight
                ),
                cornerRadius = CornerRadius.Zero,
            )
        }
    )
}