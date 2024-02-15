package com.example.clickingsimulator.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ShopItem(
    val id: Int,
    val shopItemName: String,
    val shopItemCost: Int,
    val shopItemIncrease: Int
)
