package com.aris.bmi.navhost

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aris.bmi.View
import com.aris.bmi.Women

@Composable
fun SetupNavGraph(navController: NavHostController,context: Context) {
    NavHost(navController = navController, startDestination = Page.Home.route) {
        composable(route = Page.Home.route) {
            View(navController = navController,context)
        }
        composable(
            route = Page.Second.route + "/{params}",
            arguments = listOf(navArgument("params") { type = NavType.FloatType })
        ) {
            val params = it.arguments?.getFloat("params")
            params?.let {
                Women(navController = navController, bmi = params)
            }


        }
    }
}
