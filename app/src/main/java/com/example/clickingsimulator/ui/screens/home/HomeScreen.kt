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
import com.example.clickingsimulator.data.models.ShopItem


/**
 * the click simulator app
 * @author Ömer Aynaci
 * @param homeViewModel the home view model
 */
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
            ProgressSection(viewModel = homeViewModel)
            HomeScreen(
                shopItemUiState = homeViewModel.shopItemUiState,
                onClick = { shopItem ->
                    homeViewModel.buyHandItem(shopItem)
                },
                onPassiveClick = {
                    homeViewModel.clickPerSecond()
                })
        }
    }
}

/**
 * component fo the progress section, so displaying the needed components
 * @author Ömer Aynaci
 * @param viewModel the home viewmodel
 */
@Composable
fun ProgressSection(
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

/**
 * component of the home screen if nothing goes wrong it displays the home body component [HomeBody]
 * @author Ömer Aynaci
 * @param shopItemUiState the ui state of the shop item
 * @param onClick click event
 * @param onPassiveClick passive click event
 */
@Composable
fun HomeScreen(
    shopItemUiState: ShopItemUiState,
    onClick: (ShopItem) -> Unit,
    onPassiveClick: (ShopItem) -> Unit
) {
    when (shopItemUiState) {
        is ShopItemUiState.Success -> HomeBody(
            shopItemList = shopItemUiState.shopItems,
            onClick = onClick,
            onPassiveClick = onPassiveClick
        )

        else -> {}
    }
}

/**
 * component for displaying the home body
 * @author Ömer Aynaci
 * @param shopItemList a list of shop items
 * @param onClick click event on the card
 * @param onPassiveClick passive click event on the card
 * @param viewModel the home view model
 */
@Composable
fun HomeBody(
    shopItemList: List<ShopItem>,
    onClick: (ShopItem) -> Unit,
    onPassiveClick: (ShopItem) -> Unit,
    viewModel: HomeViewModel = viewModel()
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

/**
 * component for increasing the progress bar
 * @author Ömer Aynaci
 * @param viewModel the home viewmodel
 */
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

/**
 * component for displaying a list of shop items
 * @author Ömer Aynaci
 * @param shopItems a list of shop items
 * @param onClick the click event for the shop item
 * @param onPassiveClick the passive click event for the shop item
 */
@Composable
fun ListOfShopItems(
    shopItems: List<ShopItem>,
    onClick: (ShopItem) -> Unit,
    onPassiveClick: (ShopItem) -> Unit,
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

/**
 * component card for the shop items
 * @author Ömer Aynaci
 * @param shopItem the shop item properties
 * @param onClick if click count is enough to buy an shop item then it applies to click count
 * @param onPassiveClick per second adds to the click count
 */
@Composable
fun ClickableItemCard(
    shopItem: ShopItem,
    onClick: (ShopItem) -> Unit,
    onPassiveClick: (ShopItem) -> Unit,
    viewModel: HomeViewModel = viewModel()
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

/**
 * component for the progress bar
 * @author Ömer Aynaci
 * @param progress showing the progress of the progress bar
 */
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