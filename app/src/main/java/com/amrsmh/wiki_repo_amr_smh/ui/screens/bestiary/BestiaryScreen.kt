package com.amrsmh.wiki_repo_amr_smh.ui.screens.bestiary

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amrsmh.wiki_repo_amr_smh.domain.models.Monster
import com.amrsmh.wiki_repo_amr_smh.ui.screens.viewmodel.MonsterViewModel
import com.amrsmh.wiki_repo_amr_smh.ui.screens.viewmodel.MonsterViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BestiaryScreen(
    navigateToDetail: (Long) -> Unit,
    navigateBack: () -> Unit
) {
    val vm: MonsterViewModel = viewModel(factory = MonsterViewModelFactory())
    val state by vm.uiState.collectAsState()
    val isDialogOpen by vm.isAddDialogOpen.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bestiario") },
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
            if (state.monsters.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No hay monstruos todavía. ¡Añade uno!",
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
                    items(state.monsters, key = { it.id }) { monster ->
                        MonsterCard(
                            monster = monster,
                            onClick = { navigateToDetail(monster.id) },
                            onToggleFavorite = { vm.toggleFavorite(monster) }
                        )
                    }
                }
            }

            if (isDialogOpen) {
                AddMonsterDialog(
                    onConfirm = {
                        vm.addMonster(it.copy(id = 0L, createdAt = System.currentTimeMillis()))
                        vm.showAddDialog(false)
                    },
                    onDismiss = { vm.showAddDialog(false) }
                )
            }
        }
    }
}

@Composable
private fun MonsterCard(
    monster: Monster,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val dangerColor = when (monster.danger) {
        1 -> Color(0xFF28A745) // Verde
        2 -> Color(0xFFFFA500) // Amarillo/Naranja
        3 -> Color(0xFFDC3545) // Rojo
        else -> Color.Gray
    }

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
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = monster.name,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Surface(
                        color = dangerColor,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "Peligro: ${monster.danger}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Detección: ${monster.detection}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (monster.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (monster.isFavorite) "Quitar de favoritos" else "Añadir a favoritos",
                    tint = if (monster.isFavorite) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMonsterDialog(onConfirm: (Monster) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var danger by remember { mutableStateOf(1) }
    var detection by remember { mutableStateOf("Visión") }
    var notes by remember { mutableStateOf("") }

    var expandedDanger by remember { mutableStateOf(false) }
    var expandedDetection by remember { mutableStateOf(false) }

    val dangerOptions = listOf(1, 2, 3)
    val detectionOptions = listOf("Visión", "Audio")

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val monster = Monster(
                        name = name.ifBlank { "Desconocido" },
                        danger = danger,
                        detection = detection,
                        notes = notes.ifBlank { "Sin notas" }
                    )
                    onConfirm(monster)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Añadir", color = MaterialTheme.colorScheme.onSecondary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        },
        title = { Text("Nuevo Monstruo", color = MaterialTheme.colorScheme.secondary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Dropdown para Nivel de Peligro
                ExposedDropdownMenuBox(
                    expanded = expandedDanger,
                    onExpandedChange = { expandedDanger = !expandedDanger }
                ) {
                    OutlinedTextField(
                        value = "Nivel $danger",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Nivel de Peligro") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDanger) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDanger,
                        onDismissRequest = { expandedDanger = false }
                    ) {
                        dangerOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text("Nivel $option") },
                                onClick = {
                                    danger = option
                                    expandedDanger = false
                                }
                            )
                        }
                    }
                }

                // Dropdown para Método de Detección
                ExposedDropdownMenuBox(
                    expanded = expandedDetection,
                    onExpandedChange = { expandedDetection = !expandedDetection }
                ) {
                    OutlinedTextField(
                        value = detection,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Método de Detección") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDetection) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDetection,
                        onDismissRequest = { expandedDetection = false }
                    ) {
                        detectionOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    detection = option
                                    expandedDetection = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notas Tácticas") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}