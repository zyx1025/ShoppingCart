package com.example.marsphotos.model

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Cart : Screen("cart")
    //object Order : Screen("order")
    object UserMenu : Screen("user")
}
