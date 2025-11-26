package com.amrsmh.wiki_repo_amr_smh.ui.screens.bestiary

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
                title = { Text(monster?.name ?: "Monster Detail") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    monster?.let { m ->
                        IconButton(onClick = { vm.toggleFavorite(m) }) {
                            Icon(
                                if (m.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
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
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.onSecondary)
                }
                FloatingActionButton(
                    onClick = { showDeleteDialog = true },
                    containerColor = MaterialTheme.colorScheme.error
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.onError)
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
                MonsterDetailCard("Danger Level", "${m.danger}/5")
                MonsterDetailCard("Detection Method", m.detection)
                if (!m.weaknesses.isNullOrBlank()) {
                    MonsterDetailCard("Weaknesses", m.weaknesses)
                }
                MonsterDetailCard("Tactical Notes", m.notes)
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
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text("Cancel")
                        }
                    },
                    title = { Text("Delete Monster?") },
                    text = { Text("Are you sure you want to delete '${m.name}'?") }
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

@Composable
fun EditMonsterDialog(monster: Monster, onConfirm: (Monster) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf(monster.name) }
    var danger by remember { mutableStateOf(monster.danger.toString()) }
    var detection by remember { mutableStateOf(monster.detection) }
    var notes by remember { mutableStateOf(monster.notes) }
    var weaknesses by remember { mutableStateOf(monster.weaknesses ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val updated = monster.copy(
                        name = name.ifBlank { "Unknown" },
                        danger = danger.toIntOrNull()?.coerceIn(1, 5) ?: 1,
                        detection = detection.ifBlank { "Unknown" },
                        notes = notes.ifBlank { "No notes" },
                        weaknesses = weaknesses.ifBlank { null }
                    )
                    onConfirm(updated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Edit Monster", color = MaterialTheme.colorScheme.secondary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = danger,
                    onValueChange = { danger = it },
                    label = { Text("Danger Level (1-5)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = detection,
                    onValueChange = { detection = it },
                    label = { Text("Detection Method") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = weaknesses,
                    onValueChange = { weaknesses = it },
                    label = { Text("Weaknesses (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Tactical Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        }
    )
}