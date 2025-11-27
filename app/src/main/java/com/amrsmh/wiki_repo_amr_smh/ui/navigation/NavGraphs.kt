package com.amrsmh.wiki_repo_amr_smh.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.amrsmh.wiki_repo_amr_smh.ui.screens.bestiary.BestiaryScreen
import com.amrsmh.wiki_repo_amr_smh.ui.screens.bestiary.BestiaryDetailScreen
import com.amrsmh.wiki_repo_amr_smh.ui.screens.loot.LootDetailScreen
import com.amrsmh.wiki_repo_amr_smh.ui.screens.loot.LootScreen
import com.amrsmh.wiki_repo_amr_smh.ui.screens.main.MainMenuScreen
import com.amrsmh.wiki_repo_amr_smh.ui.screens.shop.ShopScreen
import com.amrsmh.wiki_repo_amr_smh.ui.screens.shop.ShopDetailScreen
import com.amrsmh.wiki_repo_amr_smh.ui.screens.settings.SettingsScreen

@Composable
fun RepoNavHost(startDestination: String = "main_menu") {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {

        // Main Menu
        composable("main_menu") {
            MainMenuScreen(
                navigateToLoot = { navController.navigate("loot_list") },
                navigateToBestiary = { navController.navigate("bestiary") },
                navigateToShop = { navController.navigate("shop") },
                navigateToSettings = { navController.navigate("settings") }
            )
        }

        // Loot list
        composable("loot_list") {
            LootScreen(
                navigateToDetail = { id -> navController.navigate("loot_detail/$id") },
                navigateBack = { navController.popBackStack() }
            )
        }

        // Loot detail
        composable(
            route = "loot_detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            LootDetailScreen(id = id, navigateBack = { navController.popBackStack() })
        }

        // Bestiary
        composable("bestiary") {
            BestiaryScreen(
                navigateToDetail = { id -> navController.navigate("bestiary_detail/$id") },
                navigateBack = { navController.popBackStack() }
            )
        }

        // Bestiary detail
        composable(
            route = "bestiary_detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            BestiaryDetailScreen(id = id, navigateBack = { navController.popBackStack() })
        }

        // Shop
        composable("shop") {
            ShopScreen(
                navigateToDetail = { id -> navController.navigate("shop_detail/$id") },
                navigateBack = { navController.popBackStack() }
            )
        }

        // Shop detail
        composable(
            route = "shop_detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            ShopDetailScreen(id = id, navigateBack = { navController.popBackStack() })
        }

        // Settings
        composable("settings") {
            SettingsScreen(navigateBack = { navController.popBackStack() })
        }
    }
}