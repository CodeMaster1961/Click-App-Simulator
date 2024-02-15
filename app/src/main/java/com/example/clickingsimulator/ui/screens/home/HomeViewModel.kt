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
import java.text.NumberFormat
import java.util.Locale

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

    /**
     * sets passive click per second
     * @author Ömer Aynaci
     * @param shopItem the shop item properties
     */
    private fun setPassiveClickPerSecond(shopItem: ShopItem) {
        clickPerSecond = shopItem.shopItemIncrease
    }


    /**
     * gets all the shop items
     * @author Ömer Aynaci
     */
    private fun getAllItems() {
        viewModelScope.launch {
            shopItemUiState = ShopItemUiState.Loading
            try {
                val shopItems = shopItemRepository.getShopItems()
                checkIfShopItemListIsEmpty(shopItems)
            } catch (error: IOException) {
                Log.e("NetworkError", "Error fetching shop items: $error")
                ShopItemUiState.Error
            } catch (error: HttpException) {
                Log.e("HttpError", "HTTP error fetching shop items: $error")
                ShopItemUiState.Error
            }
        }
    }

    /**
     * checks if the shop items list of empty
     * @author Ömer Aynaci
     * @param shopItems a list of shop items
     */
    private fun checkIfShopItemListIsEmpty(shopItems: List<ShopItem>) {
        if (shopItems.isEmpty()) {
            shopItemUiState = ShopItemUiState.Error
        } else {
            shopItemUiState = ShopItemUiState.Success(shopItems)
        }
    }

    /**
     * formats the click count so when the click count hits over 1000 it then formats it like this, 1,000.
     * @author Ömer Aynaci
     * @return the formatted click count
     */
    fun formatClickCount(): String {
        return NumberFormat.getNumberInstance(Locale.US)
            .format(clickCount + calculateClickPerSecond())
    }

    /**
     * formats the shop item price
     * @author Ömer Aynaci
     * @return the formatted shop item price
     */
    fun formatShopItemPrice(shopItemPrice: ShopItem): String {
        return NumberFormat.getNumberInstance(Locale.US).format(shopItemPrice.shopItemCost)
    }

    /**
     * formats the shop item CPS
     * @author Ömer Aynaci
     * @return the formatted shop item CPS
     */
    fun formatShopItemClickPerSecond(shopItem: ShopItem): String {
        return NumberFormat.getNumberInstance(Locale.US).format(shopItem.shopItemIncrease)
    }

    /**
     * if an user purchases a shop item it then adds it to the total CPS
     * and when an user purchases a shop item with enough click count is subtracts the shop item cost
     * @author Ömer Aynaci
     * @param shopItem the shop item properties
     */
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

    /**
     * Calculates the sum of the shop item CPS
     * @author Ömer Aynaci
     * @return the sum of shop item CPS
     */
    fun calculateClickPerSecond(): Int {
        return purchasedItems.sumOf { it.shopItemIncrease }
    }


    /**
     * when a user purchases a shop item it then applies it to
     * the click count and then increases by each second
     * @author Ömer Aynaci
     */
    fun clickPerSecond() {
        viewModelScope.launch {
            repeat(Int.MAX_VALUE) {
                delay(1000)
                clickCount += clickPerSecond
            }
        }
    }

    /**
     * checking if the progress hits 100 or higher if so
     * then it increases the progress by 1 and the click count increases by 1
     * @author Ömer Aynaci
     */
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