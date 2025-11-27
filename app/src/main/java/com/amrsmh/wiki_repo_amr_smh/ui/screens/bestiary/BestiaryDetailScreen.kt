package com.amrsmh.wiki_repo_amr_smh.ui.screens.bestiary

import androidx.compose.foundation.layout.*
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
fun BestiaryDetailScreen(id: Long, navigateBack: () -> Unit) {
    val vm: MonsterViewModel = viewModel(factory = MonsterViewModelFactory())
    val monsterFlow = remember(id) { vm.observeById(id) }
    val monster by monsterFlow.collectAsState(initial = null)
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(monster?.name ?: "Detalle del Monstruo") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    monster?.let { m ->
                        IconButton(onClick = { vm.toggleFavorite(m) }) {
                            Icon(
                                if (m.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorito",
                                tint = if (m.isFavorite) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
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
        monster?.let { m ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MonsterDetailCard("Nivel de Peligro", "${m.danger}/3")
                MonsterDetailCard("Método de Detección", m.detection)
                MonsterDetailCard("Notas Tácticas", m.notes)
            }

            if (showEditDialog) {
                EditMonsterDialog(
                    monster = m,
                    onConfirm = { updated ->
                        vm.updateMonster(updated)
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
                                vm.deleteMonster(m.id)
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
                    title = { Text("¿Eliminar Monstruo?") },
                    text = { Text("¿Estás seguro de que quieres eliminar '${m.name}'?") }
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
private fun MonsterDetailCard(label: String, value: String) {
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
fun EditMonsterDialog(monster: Monster, onConfirm: (Monster) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf(monster.name) }
    var danger by remember { mutableStateOf(monster.danger) }
    var detection by remember { mutableStateOf(monster.detection) }
    var notes by remember { mutableStateOf(monster.notes) }

    var expandedDanger by remember { mutableStateOf(false) }
    var expandedDetection by remember { mutableStateOf(false) }

    val dangerOptions = listOf(1, 2, 3)
    val detectionOptions = listOf("Visión", "Audio")

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val updated = monster.copy(
                        name = name.ifBlank { "Desconocido" },
                        danger = danger,
                        detection = detection,
                        notes = notes.ifBlank { "Sin notas" }
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
        title = { Text("Editar Monstruo", color = MaterialTheme.colorScheme.secondary) },
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
        }
    )
}