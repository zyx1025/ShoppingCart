package com.example.marsphotos.ui.screens

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.marsphotos.model.Screen

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomNavigation(
        backgroundColor = Color.White // 设置背景色为白色
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        BottomNavigationItem(
            icon = {
                val tint = if (currentRoute == Screen.Home.route) Color.Red else Color.Black
                Icon(Icons.Default.Home, contentDescription = "Cart", tint = tint)
            },
            label = {
                val textColor = if (currentRoute == Screen.Home.route) Color.Red else Color.Black
                Text("首页", color = textColor)
            },
            selected = currentRoute == Screen.Home.route,
            onClick = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        BottomNavigationItem(
            icon = {
                val tint = if (currentRoute == Screen.Cart.route) Color.Red else Color.Black
                Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = tint)
            },
            label = {
                val textColor = if (currentRoute == Screen.Cart.route) Color.Red else Color.Black
                Text("购物车", color = textColor)
            },
            selected = currentRoute == Screen.Cart.route,
            onClick = {
                navController.navigate(Screen.Cart.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        BottomNavigationItem(
            icon = {
                val tint = if (currentRoute == Screen.UserMenu.route) Color.Red else Color.Black
                Icon(Icons.Default.Person, contentDescription = "UserMenu", tint = tint)
            },
            label = {
                val textColor = if (currentRoute == Screen.UserMenu.route) Color.Red else Color.Black
                Text("我的", color = textColor)
            },
            selected = currentRoute == Screen.UserMenu.route,
            onClick = {
                navController.navigate(Screen.UserMenu.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}