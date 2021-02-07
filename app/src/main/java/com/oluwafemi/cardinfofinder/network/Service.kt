package com.oluwafemi.cardinfofinder.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.oluwafemi.cardinfofinder.network.NetworkResponse.NetworkResponse
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl("https://lookup.binlist.net")
    .build()

interface CardDetailsService {
    @GET("/{cardNumber}")
    fun fetchCardDetailsAsync(
        @Path("cardNumber", encoded = true) cardNumber: Long
    ): Deferred<NetworkResponse>
}

object CardDetailsAPI {
    val retrofitService: CardDetailsService by lazy {
        retrofit.create(CardDetailsService::class.java)
    }
}