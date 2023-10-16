/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.marsphotos.network

import com.example.marsphotos.model.Good
import com.example.marsphotos.model.GoodInCart
import com.example.marsphotos.model.Order

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

private const val BASE_URL = "http://82.156.175.138:8080/json/"

@Serializable
data class ApiResponseForGood(
    val code: Int,
    val message: String,
    val data: List<Good>
)

@Serializable
data class ApiResponseForCart(
    val code: Int,
    val message: String,
    val data: List<GoodInCart>
)

@Serializable
data class ApiResponseForOrder(
    val code: Int,
    val message: String,
    val data: Order
)

@Serializable
data class AddGoodToCartRequest(
    val id: Int,
    val num: Int
)

@Serializable
data class AddGoodToCartResponse(
    val code: Int,
    val message: String
)

@Serializable
data class UpdateGoodToCartResponse(
    val code: Int,
    val message: String
)

@Serializable
data class DeleteGoodToCartResponse(
    val code: Int,
    val message: String
)

@Serializable
data class UpdateGoodToCartRequest(
    val id: Int,
    val num: Int
)
/**
 * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface MarsApiService {
    @GET("goods")
    suspend fun getGoods(): ApiResponseForGood

    @GET("cart")
    suspend fun getCart(): ApiResponseForCart

    @POST("order")
    suspend fun postOrder(): ApiResponseForOrder

    @POST("cart")
    suspend fun postCart(@Body requestBody: AddGoodToCartRequest): AddGoodToCartResponse

    @PUT("cart")
    suspend fun updateCart(@Body requestBody: UpdateGoodToCartRequest): UpdateGoodToCartResponse

    @DELETE("cart/{id}")
    suspend fun deleteItemFromCart(@Path("id") id: Int): DeleteGoodToCartResponse
}



/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object MarsApi {
    val retrofitService: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
}
