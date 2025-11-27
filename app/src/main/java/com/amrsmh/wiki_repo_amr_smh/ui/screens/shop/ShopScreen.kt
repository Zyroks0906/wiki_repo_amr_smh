package com.amrsmh.wiki_repo_amr_smh.ui.screens.shop

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amrsmh.wiki_repo_amr_smh.domain.models.ShopItem
import com.amrsmh.wiki_repo_amr_smh.ui.screens.viewmodel.ShopViewModel
import com.amrsmh.wiki_repo_amr_smh.ui.screens.viewmodel.ShopViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    navigateToDetail: (Long) -> Unit,
    navigateBack: () -> Unit
) {
    val vm: ShopViewModel = viewModel(factory = ShopViewModelFactory())
    val state by vm.uiState.collectAsState()
    val isDialogOpen by vm.isAddDialogOpen.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tienda") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { vm.showAddDialog(true) },
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir", tint = MaterialTheme.colorScheme.onSecondary)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (state.items.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No hay artículos todavía. ¡Añade uno!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.items, key = { it.id }) { item ->
                        ShopItemCard(
                            item = item,
                            onClick = { navigateToDetail(item.id) },
                            onToggleFavorite = { vm.toggleFavorite(item) }
                        )
                    }
                }
            }

            if (isDialogOpen) {
                AddShopItemDialog(
                    onConfirm = {
                        vm.addItem(it.copy(id = 0L, createdAt = System.currentTimeMillis()))
                        vm.showAddDialog(false)
                    },
                    onDismiss = { vm.showAddDialog(false) }
                )
            }
        }
    }
}

@Composable
private fun ShopItemCard(
    item: ShopItem,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "${item.price}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = item.category,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (item.isFavorite) "Quitar de la lista de deseos" else "Añadir a la lista de deseos",
                    tint = if (item.isFavorite) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddShopItemDialog(onConfirm: (ShopItem) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Arma") }

    var expandedCategory by remember { mutableStateOf(false) }
    val categoryOptions = listOf("Arma", "Consumible", "Utilidad")

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val item = ShopItem(
                        name = name.ifBlank { "Sin nombre" },
                        price = price.toIntOrNull() ?: 0,
                        description = description.ifBlank { "Sin descripción" },
                        category = category
                    )
                    onConfirm(item)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Añadir", color = MaterialTheme.colorScheme.onSecondary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        },
        title = { Text("Nuevo Artículo", color = MaterialTheme.colorScheme.secondary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Dropdown para Categoría
                ExposedDropdownMenuBox(
                    expanded = expandedCategory,
                    onExpandedChange = { expandedCategory = !expandedCategory }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false }
                    ) {
                        categoryOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    category = option
                                    expandedCategory = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}