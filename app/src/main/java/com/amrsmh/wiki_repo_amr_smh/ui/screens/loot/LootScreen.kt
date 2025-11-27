package com.amrsmh.wiki_repo_amr_smh.ui.screens.loot

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
fun LootScreen(
    navigateToDetail: (Long) -> Unit,
    navigateBack: () -> Unit
) {
    val vm: LootViewModel = viewModel(factory = LootViewModelFactory())
    val state by vm.uiState.collectAsState()
    val isDialogOpen by vm.isAddDialogOpen.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Botín") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { vm.showAddDialog(true) }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (state.items.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                        Text(
                            text = "No hay objetos todavía",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Toca + para añadir tu primer objeto",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(state.items, key = { it.id }) { item ->
                        LootCard(
                            item = item,
                            onClick = { navigateToDetail(item.id) },
                            onToggleFavorite = { vm.toggleFavorite(item) }
                        )
                    }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLootDialog(onConfirm: (LootItem) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("Genérico") }
    var valueStr by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("Normal") }
    var notes by remember { mutableStateOf("") }

    var expandedLocation by remember { mutableStateOf(false) }
    var expandedWeight by remember { mutableStateOf(false) }

    val locationOptions = listOf("Genérico", "Mágico", "Mansión", "Ártico", "Museo")
    val weightOptions = listOf("Ligero", "Normal", "Pesado", "Muy Pesado")

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val value = valueStr.toIntOrNull() ?: 0
                val item = LootItem(
                    name = name.ifBlank { "Sin nombre" },
                    location = location,
                    value = value,
                    weight = weight,
                    notes = notes.ifBlank { null },
                    createdAt = System.currentTimeMillis(),
                    isFavorite = false
                )
                onConfirm(item)
            }) {
                Text("Añadir")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        },
        title = { Text("Nuevo Objeto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Dropdown para Ubicación
                ExposedDropdownMenuBox(
                    expanded = expandedLocation,
                    onExpandedChange = { expandedLocation = !expandedLocation }
                ) {
                    OutlinedTextField(
                        value = location,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Ubicación") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLocation) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedLocation,
                        onDismissRequest = { expandedLocation = false }
                    ) {
                        locationOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    location = option
                                    expandedLocation = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = valueStr,
                    onValueChange = { valueStr = it },
                    label = { Text("Valor") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Dropdown para peso
                ExposedDropdownMenuBox(
                    expanded = expandedWeight,
                    onExpandedChange = { expandedWeight = !expandedWeight }
                ) {
                    OutlinedTextField(
                        value = weight,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Peso") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedWeight) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedWeight,
                        onDismissRequest = { expandedWeight = false }
                    ) {
                        weightOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    weight = option
                                    expandedWeight = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notas (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        }
    )
}
