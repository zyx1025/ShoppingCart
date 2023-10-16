package com.example.marsphotos.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter

import com.example.marsphotos.model.GoodInCart
import com.example.marsphotos.ui.viewmodel.CartUIState
import com.example.marsphotos.ui.viewmodel.GoodsViewModel

//解决remember报错
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.marsphotos.network.AddGoodToCartRequest
import com.example.marsphotos.network.MarsApi
import com.example.marsphotos.ui.theme.PurpleGrey80
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun CartScreen(
    cartUiState: CartUIState, modifier: Modifier = Modifier, viewModel: GoodsViewModel,
) {
    val viewModel: GoodsViewModel = viewModel()



    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    when (cartUiState) {
        is CartUIState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is CartUIState.Success -> {
            Scaffold(
                bottomBar = { BottomBar(cartUiState.products, viewModel = viewModel, modifier = Modifier.fillMaxWidth(), scope) }
            ) { paddingValues ->
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                    onRefresh = {
                        scope.launch {
                            isRefreshing = true
                            viewModel.getCart()
                            isRefreshing = false
                        }
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = paddingValues.calculateBottomPadding())
                    ) {
                        ResultCartScreen(
                            cartUiState.products,
                            modifier = Modifier.weight(1f),
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
        is CartUIState.Error -> ErrorScreen(modifier = modifier.fillMaxSize())
    }
}


/*
显示多少条
 */
@Composable
fun BottomBar(
    products: List<GoodInCart>,
    viewModel: GoodsViewModel,
    modifier: Modifier = Modifier,
    scope: CoroutineScope
) {
    // 计算所有商品的总价格
    val totalPrice = products.sumOf { it.price * it.num }
    val totalNum = products.sumOf { it.num }
    val context = LocalContext.current

    Log.d("BottomBar", "Rendering BottomBar with total price: $totalPrice")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        val text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontSize = 15.sp)) {
                append("已选")
                append(totalNum.toString())
                append("件，合计:￥ ")
            }
            withStyle(style = SpanStyle(color = Color.Red, fontSize = 20.sp)) {
                append(totalPrice.toString())
            }
        }
        Text(text = text)
        Button(
            onClick = {
                scope.launch {
                    val response = MarsApi.retrofitService.postOrder()
                    //这个接口写的不好
                    if (response.code == response.data.totalnum) {
                        var goodsViewModel=GoodsViewModel()
                        Toast.makeText(context,"已提交订单",Toast.LENGTH_SHORT).show()
                        goodsViewModel.getCart();
                    } else {
                        // 处理失败情况
                        println("提交订单失败: ${response.message}")
                    }
                }
            },
        ) {
            Text(text = "提交订单")
        }
    }
}



/**
 * ResultScreen displaying number of photos retrieved.
 */
@Composable
fun ResultCartScreen(products: List<GoodInCart>, modifier: Modifier = Modifier,viewModel: GoodsViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = products) { product ->  // 注意这里的修改
            ProductInCartItem(product,viewModel)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

/*
单独渲染每一条记录
 */
@Composable
fun ProductInCartItem(product: GoodInCart,viewModel: GoodsViewModel) {
    var quantity by remember { mutableStateOf(product.num.toString()) }

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
            Image(
                painter = rememberAsyncImagePainter(model = product.image),
                contentDescription = "Product Image",
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = product.name,fontSize = 15.sp)
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "￥ "+product.price.toString(), color = Color.Red, fontSize = 20.sp)

                    Spacer(modifier = Modifier.width(30.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                if (quantity.toIntOrNull() != null) {
                                    val newQuantity = quantity.toInt() - 1
                                    if (newQuantity >= 1) {
                                        quantity = newQuantity.toString()
                                        viewModel.updateCart(product.id, newQuantity)
                                    }
                                }
                            },
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape),
                            enabled = quantity.toIntOrNull() != null && quantity.toInt() > 1,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ){
                                Text(text = "-", fontSize = 10.sp)
                            }
                        }

                        Text(
                            text = quantity,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                        )

                        Button(
                            onClick = {
                                if (quantity.toIntOrNull() != null) {
                                    val newQuantity = quantity.toInt() + 1
                                    quantity = newQuantity.toString()
                                    viewModel.updateCart(product.id, newQuantity)
                                 }
                                      },
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ){
                                Text(text = "+", fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
            Button(
                onClick = { viewModel.removeItemFromCart(product.id) },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurpleGrey80,
                    contentColor = Color.Black
                ),
                contentPadding = PaddingValues(0.dp)  // 移除内边距以使文本居中
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "×", color = Color.Black, fontSize = 20.sp)
                }
            }

        }
    }
}

