package com.aris.bmi.navhost

sealed class Page(val route: String) {
    object Home : Page("home_page")
    object Second : Page("second_page")
    object Third : Page("Third_page")
}

