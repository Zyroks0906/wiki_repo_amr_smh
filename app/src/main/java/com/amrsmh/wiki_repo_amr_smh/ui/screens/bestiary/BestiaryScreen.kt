package com.amrsmh.wiki_repo_amr_smh.ui.screens.bestiary

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BestiaryScreen(navigateBack: () -> Unit) {
    var monsters by remember {
        mutableStateOf(
            listOf(
                Monster(1, "Huntsman", 3, "Sound-based detection", "Stay quiet, use thrown decoys"),
                Monster(2, "Hidden", 3, "Stealth grabber", "Check corners, listen for breathing"),
                Monster(3, "Animal", 2, "Aggressive, affected by thrown objects", "Distract with throwables"),
                Monster(4, "Screamer", 4, "Sound-based alert", "Kill quickly before alert")
            )
        )
    }
    var showDialog by remember { mutableStateOf(false) }
    var editingMonster by remember { mutableStateOf<Monster?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bestiary") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editingMonster = null
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Monster")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(monsters) { monster ->
                MonsterCard(
                    monster = monster,
                    onEdit = {
                        editingMonster = monster
                        showDialog = true
                    },
                    onDelete = {
                        monsters = monsters.filter { it.id != monster.id }
                    }
                )
            }
        }

        if (showDialog) {
            MonsterDialog(
                monster = editingMonster,
                onDismiss = { showDialog = false },
                onConfirm = { name, danger, detection, notes ->
                    if (editingMonster != null) {
                        monsters = monsters.map {
                            if (it.id == editingMonster!!.id) {
                                it.copy(name = name, danger = danger, detection = detection, notes = notes)
                            } else it
                        }
                    } else {
                        val newId = (monsters.maxOfOrNull { it.id } ?: 0) + 1
                        monsters = monsters + Monster(newId, name, danger, detection, notes)
                    }
                    showDialog = false
                }
            )
        }
    }
}

@Composable
private fun MonsterCard(
    monster: Monster,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = monster.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Row {
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
                    Spacer(Modifier.width(8.dp))
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Detection: ${monster.detection}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Tactical notes:",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = monster.notes,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit")
            }
        }
    }
}

@Composable
private fun MonsterDialog(
    monster: Monster?,
    onDismiss: () -> Unit,
    onConfirm: (String, Int, String, String) -> Unit
) {
    var name by remember { mutableStateOf(monster?.name ?: "") }
    var dangerStr by remember { mutableStateOf(monster?.danger?.toString() ?: "1") }
    var detection by remember { mutableStateOf(monster?.detection ?: "") }
    var notes by remember { mutableStateOf(monster?.notes ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val danger = dangerStr.toIntOrNull() ?: 1
                onConfirm(name, danger, detection, notes)
            }) {
                Text(if (monster == null) "Add" else "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text(if (monster == null) "New Monster" else "Edit Monster") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = dangerStr,
                    onValueChange = { dangerStr = it },
                    label = { Text("Danger (1-5)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = detection,
                    onValueChange = { detection = it },
                    label = { Text("Detection Method") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Tactical Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }
        }
    )
}

private data class Monster(
    val id: Long,
    val name: String,
    val danger: Int,
    val detection: String,
    val notes: String
)