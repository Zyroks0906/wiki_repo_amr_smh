package com.amrsmh.wiki_repo_amr_smh.ui.screens.loot

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amrsmh.wiki_repo_amr_smh.domain.models.LootItem
import com.amrsmh.wiki_repo_amr_smh.ui.screens.viewmodel.LootViewModel
import com.amrsmh.wiki_repo_amr_smh.ui.screens.viewmodel.LootViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LootDetailScreen(id: Long, navigateBack: () -> Unit) {
    val vm: LootViewModel = viewModel(factory = LootViewModelFactory())
    val itemFlow = remember(id) { vm.observeById(id) }
    val item by itemFlow.collectAsState(initial = null)
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(item?.name ?: "Detalle del Botín") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    item?.let { loot ->
                        IconButton(onClick = { vm.toggleFavorite(loot) }) {
                            Icon(
                                if (loot.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorito",
                                tint = if (loot.isFavorite) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                FloatingActionButton(
                    onClick = { showEditDialog = true },
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.onSecondary)
                }
                FloatingActionButton(
                    onClick = { showDeleteDialog = true },
                    containerColor = MaterialTheme.colorScheme.error
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.onError)
                }
            }
        }
    ) { padding ->
        item?.let { loot ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DetailCard("Ubicación", loot.location)
                DetailCard("Valor", "${loot.value}")
                DetailCard("Peso", loot.weight)
                if (!loot.notes.isNullOrBlank()) {
                    DetailCard("Notas", loot.notes)
                }
            }

            if (showEditDialog) {
                EditLootDialog(
                    item = loot,
                    onConfirm = { updated ->
                        vm.updateItem(updated)
                        showEditDialog = false
                    },
                    onDismiss = { showEditDialog = false }
                )
            }

            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    confirmButton = {
                        Button(
                            onClick = {
                                vm.deleteItem(loot.id)
                                navigateBack()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Eliminar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text("Cancelar")
                        }
                    },
                    title = { Text("¿Eliminar objeto?") },
                    text = { Text("¿Estás seguro de que quieres eliminar '${loot.name}'? Esta acción no se puede deshacer.") }
                )
            }
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Composable
private fun DetailCard(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLootDialog(item: LootItem, onConfirm: (LootItem) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf(item.name) }
    var location by remember { mutableStateOf(item.location) }
    var valueStr by remember { mutableStateOf(item.value.toString()) }
    var weight by remember { mutableStateOf(item.weight) }
    var notes by remember { mutableStateOf(item.notes ?: "") }

    var expandedLocation by remember { mutableStateOf(false) }
    var expandedWeight by remember { mutableStateOf(false) }

    val locationOptions = listOf("Genérico", "Mágico", "Mansión", "Ártico", "Museo")
    val weightOptions = listOf("Ligero", "Normal", "Pesado", "Muy Pesado")

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val updated = item.copy(
                        name = name.ifBlank { "Sin nombre" },
                        location = location,
                        value = valueStr.toIntOrNull() ?: 0,
                        weight = weight,
                        notes = notes.ifBlank { null }
                    )
                    onConfirm(updated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        },
        title = { Text("Editar Objeto", color = MaterialTheme.colorScheme.secondary) },
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
                    label = { Text("Notas") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        }
    )
}