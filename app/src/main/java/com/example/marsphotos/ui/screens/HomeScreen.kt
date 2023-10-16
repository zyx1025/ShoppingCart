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
package com.example.marsphotos.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.marsphotos.R
import com.example.marsphotos.model.Good
import com.example.marsphotos.ui.viewmodel.GoodUIState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.marsphotos.network.AddGoodToCartRequest
import com.example.marsphotos.network.MarsApi


@Composable
fun HomeScreen(
    goodUiState: GoodUIState, modifier: Modifier = Modifier
) {
    when (goodUiState) {
        is GoodUIState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is GoodUIState.Success -> ResultGoodScreen(
            goodUiState.products, modifier = modifier.fillMaxWidth()
        )
        is GoodUIState.Error -> ErrorScreen(modifier = modifier.fillMaxSize())
    }
}

/**
 * ResultScreen displaying number of photos retrieved.
 */
@Composable
fun ResultGoodScreen(products: List<Good>, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp), // 设置内边距
        verticalArrangement = Arrangement.spacedBy(8.dp),  // 垂直间距
        horizontalArrangement = Arrangement.spacedBy(8.dp), // 水平间距
        content = {
            items(products){
                ProductItemGrid(it,scope)
            }
        }
    ) 
        
    
}


/*
单独渲染每一条记录
 */
@Composable
fun ProductItemGrid(product: Good,scope: CoroutineScope) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
                .width(150.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = product.image),
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            Text(text = product.name, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text(text = "￥ "+product.price.toString(), fontWeight = FontWeight.Light, textAlign = TextAlign.Center)
            Button(onClick = {
                scope.launch {
                    val cartRequest = AddGoodToCartRequest(id = product.id, num = 1)
                    val response = MarsApi.retrofitService.postCart(cartRequest)
                    if (response.code == 1) {
                        Toast.makeText(context,"已将本商品添加到购物车",Toast.LENGTH_SHORT).show();
                    } else {
                        // 处理失败情况
                        println("添加到购物车失败: ${response.message}")
                    }
                }
            }) {
                Text("加入购物车")
            }
        }
    }
}
