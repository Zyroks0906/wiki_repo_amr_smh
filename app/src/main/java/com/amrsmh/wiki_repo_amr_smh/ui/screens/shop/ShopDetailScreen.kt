package com.amrsmh.wiki_repo_amr_smh.ui.screens.shop

import androidx.compose.foundation.layout.*
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
fun ShopDetailScreen(id: Long, navigateBack: () -> Unit) {
    val vm: ShopViewModel = viewModel(factory = ShopViewModelFactory())
    val itemFlow = remember(id) { vm.observeById(id) }
    val item by itemFlow.collectAsState(initial = null)
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(item?.name ?: "Artículo de Tienda") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    item?.let { shopItem ->
                        IconButton(onClick = { vm.toggleFavorite(shopItem) }) {
                            Icon(
                                if (shopItem.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Lista de deseos",
                                tint = if (shopItem.isFavorite) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
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
        item?.let { shopItem ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ShopDetailCard("Precio", "${shopItem.price}")
                ShopDetailCard("Categoría", shopItem.category)
                ShopDetailCard("Descripción", shopItem.description)
            }

            if (showEditDialog) {
                EditShopItemDialog(
                    item = shopItem,
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
                                vm.deleteItem(shopItem.id)
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
                    title = { Text("¿Eliminar Artículo?") },
                    text = { Text("¿Estás seguro de que quieres eliminar '${shopItem.name}'?") }
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
private fun ShopDetailCard(label: String, value: String) {
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
fun EditShopItemDialog(item: ShopItem, onConfirm: (ShopItem) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf(item.name) }
    var price by remember { mutableStateOf(item.price.toString()) }
    var description by remember { mutableStateOf(item.description) }
    var category by remember { mutableStateOf(item.category) }

    var expandedCategory by remember { mutableStateOf(false) }
    val categoryOptions = listOf("Arma", "Consumible", "Utilidad")

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val updated = item.copy(
                        name = name.ifBlank { "Sin nombre" },
                        price = price.toIntOrNull() ?: 0,
                        description = description.ifBlank { "Sin descripción" },
                        category = category
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
        title = { Text("Editar Artículo", color = MaterialTheme.colorScheme.secondary) },
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
        }
    )
}