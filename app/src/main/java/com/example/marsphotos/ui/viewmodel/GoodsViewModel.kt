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
package com.example.marsphotos.ui.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsphotos.model.Good
import com.example.marsphotos.model.GoodInCart
import com.example.marsphotos.model.Order
import com.example.marsphotos.network.AddGoodToCartRequest
import com.example.marsphotos.network.MarsApi
import com.example.marsphotos.network.UpdateGoodToCartRequest
import com.example.marsphotos.network.UpdateGoodToCartResponse
import kotlinx.coroutines.launch

/**
 * UI state for the Home screen
 */
sealed interface GoodUIState {
    data class Success(val products: List<Good>) : GoodUIState
    object Error : GoodUIState
    object Loading : GoodUIState
}
sealed interface CartUIState {
    data class Success(val products: List<GoodInCart>) : CartUIState
    object Error : CartUIState
    object Loading : CartUIState
}
sealed interface OrderUIState {
    data class Success(val order: Order) : OrderUIState
    object Error : OrderUIState
    object Loading : OrderUIState
}

class GoodsViewModel : ViewModel() {
    var goodsUiState: GoodUIState by mutableStateOf(GoodUIState.Loading)
    var cartUiState: CartUIState by mutableStateOf(CartUIState.Loading)
    var orderUiState: OrderUIState by mutableStateOf(OrderUIState.Loading)
        private set

    //获取全部商品
    fun getGoods() {
        viewModelScope.launch {
            goodsUiState = GoodUIState.Loading
            try {
                val response = MarsApi.retrofitService.getGoods()
                goodsUiState = GoodUIState.Success(response.data)
            } catch (e: Exception) {
                Log.e("Home", "Failed to fetch goods", e)
                goodsUiState = GoodUIState.Error
            }
        }
    }

    fun getCart() {
        viewModelScope.launch {
            try {
                val response = MarsApi.retrofitService.getCart()
                cartUiState = CartUIState.Success(response.data)
            } catch (e: Exception) {
                Log.e("Cart", "Failed to fetch cart", e)
                cartUiState = CartUIState.Error
            }
        }
    }

    fun postOrder() {
        viewModelScope.launch {
            try {
                val response = MarsApi.retrofitService.postOrder()
                orderUiState = OrderUIState.Success(response.data)
            } catch (e: Exception) {
                Log.e("Order", "Failed to fetch order", e)
                orderUiState = OrderUIState.Error
            }
        }
    }

    fun updateCart(id: Int, num: Int) {
        viewModelScope.launch {
            try {
                val requestBody = UpdateGoodToCartRequest(id = id, num = num)
                val response = MarsApi.retrofitService.updateCart(requestBody)
                if (response.code == 1) {
                    getCart()
                }
            } catch (e: Exception) {
                Log.e("UpdateCart", "Failed to update cart", e)
            }
        }
    }

    // 从购物车中移除商品
    fun removeItemFromCart(id: Int) {
        viewModelScope.launch {
            try {
                val response = MarsApi.retrofitService.deleteItemFromCart(id)
                if (response.code == 1) {
                    getCart()
                }
            } catch (e: Exception) {
                Log.e("RemoveCart", "Failed to remove cart", e)
            }
        }
    }
}
