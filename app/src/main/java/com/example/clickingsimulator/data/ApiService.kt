package com.example.clickingsimulator.data

import com.example.clickingsimulator.data.models.ShopItem
import retrofit2.http.GET

interface ApiService {
    @GET("/items")
    suspend fun getAllShopItems(): List<ShopItem>
}