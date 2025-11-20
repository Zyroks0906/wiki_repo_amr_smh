package com.amrsmh.wiki_repo_amr_smh.ui.screens.loot

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amrsmh.wiki_repo_amr_smh.domain.models.LootItem
import com.amrsmh.wiki_repo_amr_smh.ui.components.LootCard
import com.amrsmh.wiki_repo_amr_smh.ui.screens.viewmodel.LootViewModel
import com.amrsmh.wiki_repo_amr_smh.ui.screens.viewmodel.LootViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LootScreen(navigateToDetail: (Long) -> Unit) {
    val vm: LootViewModel = viewModel(factory = LootViewModelFactory())
    val state by vm.uiState.collectAsState()
    val isDialogOpen by vm.isAddDialogOpen.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Loot Log") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { vm.showAddDialog(true) }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.items, key = { it.id }) { item ->
                    LootCard(
                        item = item,
                        onClick = { navigateToDetail(item.id) },
                        onToggleFavorite = { vm.toggleFavorite(item) }
                    )
                }
            }

            if (isDialogOpen) {
                AddLootDialog(
                    onConfirm = {
                        val item = it.copy(id = 0L, createdAt = System.currentTimeMillis())
                        vm.addItem(item)
                        vm.showAddDialog(false)
                    },
                    onDismiss = { vm.showAddDialog(false) }
                )
            }
        }
    }
}

@Composable
fun AddLootDialog(onConfirm: (LootItem) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("generic") }
    var valueStr by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val value = valueStr.toIntOrNull() ?: 0
                val item = LootItem(
                    name = name.ifBlank { "Unnamed" },
                    category = category,
                    rarity = "common",
                    value = value,
                    weight = 1f,
                    transportDifficulty = 1,
                    state = "extracted",
                    runId = null,
                    notes = null,
                    createdAt = System.currentTimeMillis(),
                    isFavorite = false
                )
                onConfirm(item)
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("New Loot Item") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") }
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = valueStr,
                    onValueChange = { valueStr = it },
                    label = { Text("Value (SURPLUS)") }
                )
            }
        }
    )
}