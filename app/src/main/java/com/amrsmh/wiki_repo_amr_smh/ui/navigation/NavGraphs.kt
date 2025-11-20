package com.amrsmh.wiki_repo_amr_smh.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.amrsmh.wiki_repo_amr_smh.ui.screens.bestiary.BestiaryScreen
import com.amrsmh.wiki_repo_amr_smh.ui.screens.loot.LootDetailScreen
import com.amrsmh.wiki_repo_amr_smh.ui.screens.loot.LootScreen
import com.amrsmh.wiki_repo_amr_smh.ui.screens.shop.ShopScreen
import com.amrsmh.wiki_repo_amr_smh.ui.screens.settings.SettingsScreen

@Composable
fun RepoNavHost(startDestination: String = "loot_list") {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {

        // Loot list
        composable("loot_list") {
            LootScreen(navigateToDetail = { id -> navController.navigate("loot_detail/$id") })
        }

        // Loot detail
        composable(
            "loot_detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            LootDetailScreen(id = id, navigateBack = { navController.popBackStack() })
        }

        // Bestiary
        composable("bestiary") {
            BestiaryScreen()
        }

        // Shop
        composable("shop") {
            ShopScreen()
        }

        // Settings
        composable("settings") {
            SettingsScreen()
        }
    }
}
