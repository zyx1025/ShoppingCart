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

package com.example.marsphotos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.marsphotos.model.Screen
import com.example.marsphotos.ui.screens.BottomNavigationBar
import com.example.marsphotos.ui.screens.CartScreen
import com.example.marsphotos.ui.screens.HomeScreen
import com.example.marsphotos.ui.screens.OrderScreen
import com.example.marsphotos.ui.screens.UserMenu
import com.example.marsphotos.ui.theme.MarsPhotosTheme
import com.example.marsphotos.ui.viewmodel.GoodsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            MarsPhotosTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(navController, startDestination = Screen.Home.route) {
                composable(Screen.Home.route) {
                    val goodsViewModel: GoodsViewModel = viewModel()
                    LaunchedEffect(Unit) {
                        goodsViewModel.getGoods() // 每次进入时都重新加载
                    }
                    HomeScreen(goodUiState = goodsViewModel.goodsUiState)
                }
                composable(Screen.Cart.route) {
                    val goodsViewModel: GoodsViewModel = viewModel()
                    LaunchedEffect(Unit) {
                        goodsViewModel.getCart() // 每次进入时都重新加载
                    }
                    CartScreen(cartUiState = goodsViewModel.cartUiState, viewModel = goodsViewModel)
                }
                composable(Screen.UserMenu.route) {
                    UserMenu();
                }
            }
        }
    }
}