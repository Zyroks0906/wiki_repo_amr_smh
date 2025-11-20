package com.amrsmh.wiki_repo_amr_smh.ui.screens.loot

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amrsmh.wiki_repo_amr_smh.ui.screens.viewmodel.LootViewModel
import com.amrsmh.wiki_repo_amr_smh.ui.screens.viewmodel.LootViewModelFactory

/**
 * Pantalla detalle: usa ViewModel.observeById(id) para cargar el item.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LootDetailScreen(id: Long, navigateBack: () -> Unit) {
    val vm: LootViewModel = viewModel(factory = LootViewModelFactory())
    val itemFlow = remember(id) { vm.observeById(id) }
    val item by itemFlow.collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(item?.name ?: "Loot Detail") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        item?.let { loot ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Category
                Text(
                    text = "Category: ${loot.category}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(8.dp))

                // Rarity
                Text(
                    text = "Rarity: ${loot.rarity}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))

                // Value
                Text(
                    text = "Value: ${loot.value} SURPLUS",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))

                // Weight
                Text(
                    text = "Weight: ${loot.weight} kg",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))

                // Transport Difficulty
                Text(
                    text = "Transport Difficulty: ${loot.transportDifficulty}/5",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))

                // State
                Text(
                    text = "State: ${loot.state}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))

                // Notes
                Text(
                    text = "Notes: ${loot.notes ?: "—"}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(24.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { vm.toggleFavorite(loot) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (loot.isFavorite) "Unfavorite" else "Favorite")
                    }

                    Button(
                        onClick = {
                            vm.deleteItem(loot.id)
                            navigateBack()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Delete")
                    }
                }
            }
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}