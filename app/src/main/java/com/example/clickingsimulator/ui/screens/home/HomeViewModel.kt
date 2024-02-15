package com.example.clickingsimulator.ui.screens.home


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickingsimulator.data.models.ShopItem
import com.example.clickingsimulator.data.repository.ShopItemRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.lang.RuntimeException
import java.text.NumberFormat
import java.util.Locale
import kotlin.time.Duration.Companion.seconds

sealed interface ShopItemUiState {
    data class Success(val shopItems: List<ShopItem>) : ShopItemUiState
    data object Error : ShopItemUiState
    data object Loading : ShopItemUiState
}

class HomeViewModel(private val shopItemRepository: ShopItemRepository) : ViewModel() {

    var shopItemUiState: ShopItemUiState by mutableStateOf(ShopItemUiState.Loading)

    var progress by mutableFloatStateOf(0.0f)
    var level by mutableIntStateOf(0)
    var clickCount by mutableIntStateOf(100)
    private var clickPerSecond by mutableIntStateOf(0)
    private val purchasedItems = mutableStateListOf<ShopItem>()

    init {
        getAllItems()
    }

    fun setPassiveClickPerSecond(shopItem: ShopItem) {
        clickPerSecond = shopItem.shopItemIncrease
    }


     fun getAllItems() {
        viewModelScope.launch {
            shopItemUiState = ShopItemUiState.Loading
            try {
                val shopItems = shopItemRepository.getShopItems()
                if (shopItems.isEmpty()) {
                    shopItemUiState = ShopItemUiState.Error
                } else {
                    shopItemUiState = ShopItemUiState.Success(shopItems)
                }
            } catch (error: IOException) {
                Log.e("NetworkError", "Error fetching shop items: $error")
                ShopItemUiState.Error
            } catch (error: HttpException) {
                Log.e("HttpError", "HTTP error fetching shop items: $error")
                ShopItemUiState.Error
            }
        }
    }

    fun formatClickCount(): String {
        return NumberFormat.getNumberInstance(Locale.US).format(clickCount + calculateClickPerSecond())
    }

    fun formatShopItemPrice(shopItemPrice: ShopItem): String {
        return NumberFormat.getNumberInstance(Locale.US).format(shopItemPrice.shopItemCost)
    }

    fun formatShopItemClickPerSecond(shopItem: ShopItem): String {
        return NumberFormat.getNumberInstance(Locale.US).format(shopItem.shopItemIncrease)
    }

    fun buyHandItem(shopItem: ShopItem) {
        viewModelScope.launch {
            val cost = shopItem.shopItemCost
            if (clickCount >= cost) {
                clickCount -= cost
                clickPerSecond += shopItem.shopItemIncrease
                purchasedItems.add(shopItem)
                setPassiveClickPerSecond(shopItem)
            }
        }
    }

    fun calculateClickPerSecond(): Int {
        return purchasedItems.sumOf { it.shopItemIncrease }
    }


    fun clickPerSecond() {
        viewModelScope.launch {
            repeat(Int.MAX_VALUE) {
                delay(1000)
                clickCount += clickPerSecond
            }
        }
    }

    fun updateProgress() {
        if ((progress * 100).toInt() >= 100) {
            level++
            progress = 0.0f
        } else {
            progress += 0.01f
            clickCount += 1
        }
    }
}