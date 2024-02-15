package com.example.clickingsimulator.di

import com.example.clickingsimulator.data.ApiService
import com.example.clickingsimulator.data.repository.ShopItemRepository
import com.example.clickingsimulator.data.repositoryImplementation.ShopItemRepositoryImplementation
import com.example.clickingsimulator.ui.screens.home.HomeViewModel
import com.example.clickingsimulator.util.Constants.BASE_URL
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        retrofitInstance().create(ApiService::class.java)
    }

    single<ShopItemRepository> {
        ShopItemRepositoryImplementation(get())
    }

    viewModel<HomeViewModel> {
        HomeViewModel(get())
    }
}

private fun retrofitInstance(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}