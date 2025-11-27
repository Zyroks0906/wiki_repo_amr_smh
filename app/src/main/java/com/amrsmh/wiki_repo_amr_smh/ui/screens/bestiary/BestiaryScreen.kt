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
                title = { Text("Bestiary") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
                Icon(Icons.Default.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.onSecondary)
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
                        "No monsters yet. Add one!",
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
                        color = when (monster.danger) {
                            in 1..2 -> MaterialTheme.colorScheme.tertiary
                            3 -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.error
                        },
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "Danger: ${monster.danger}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Detection: ${monster.detection}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (monster.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (monster.isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (monster.isFavorite) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun AddMonsterDialog(onConfirm: (Monster) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var danger by remember { mutableStateOf("1") }
    var detection by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var weaknesses by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val monster = Monster(
                        name = name.ifBlank { "Unknown" },
                        danger = danger.toIntOrNull()?.coerceIn(1, 5) ?: 1,
                        detection = detection.ifBlank { "Unknown" },
                        notes = notes.ifBlank { "No notes" },
                        weaknesses = weaknesses.ifBlank { null }
                    )
                    onConfirm(monster)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Add", color = MaterialTheme.colorScheme.onSecondary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("New Monster", color = MaterialTheme.colorScheme.secondary) },
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
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}