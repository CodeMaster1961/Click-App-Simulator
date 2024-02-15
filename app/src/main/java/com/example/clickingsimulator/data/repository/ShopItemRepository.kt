package com.example.clickingsimulator.data.repository

import com.example.clickingsimulator.data.models.ShopItem

interface ShopItemRepository {
    suspend fun getShopItems(): List<ShopItem>
}