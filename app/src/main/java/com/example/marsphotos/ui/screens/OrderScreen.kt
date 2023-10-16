package com.example.marsphotos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.marsphotos.model.GoodInCart
import com.example.marsphotos.model.Order
import com.example.marsphotos.ui.viewmodel.OrderUIState

/*
此界面备用
 */

@Composable
fun OrderScreen(
    orderUiState: OrderUIState, modifier: Modifier = Modifier
) {
    when (orderUiState) {
        is OrderUIState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is OrderUIState.Error -> ErrorScreen(modifier = modifier.fillMaxSize())
        is OrderUIState.Success -> ResultOrderScreen(
            orderUiState.order, modifier = modifier.fillMaxWidth()
        )
    }
}

/**
 * ResultScreen displaying number of photos retrieved.
 */
@Composable
fun ResultOrderScreen(order: Order, modifier: Modifier = Modifier) {
    Text(text = order.totalnum.toString());
    Text(text = order.totalprice.toString())
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = order.goodslist) {good ->  // 注意这里的修改
            ShowGoodsInOrderList(good)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

/*
单独渲染每一条记录
 */
@Composable
fun ShowGoodsInOrderList(good: GoodInCart) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = good.name, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(16.dp))  // 增加间距
            Text(text = good.num.toString())
        }
    }
}