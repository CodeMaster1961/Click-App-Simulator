package com.example.clickingsimulator.data.repositoryImplementation

import com.example.clickingsimulator.data.ApiService
import com.example.clickingsimulator.data.models.ShopItem
import com.example.clickingsimulator.data.repository.ShopItemRepository

class ShopItemRepositoryImplementation(
    private val shopItemApi: ApiService
) : ShopItemRepository {

    override suspend fun getShopItems(): List<ShopItem> {
        return shopItemApi.getAllShopItems()
    }
}