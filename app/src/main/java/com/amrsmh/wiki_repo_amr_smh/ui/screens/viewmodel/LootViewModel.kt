package com.amrsmh.wiki_repo_amr_smh.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrsmh.wiki_repo_amr_smh.data.repository.LootRepository
import com.amrsmh.wiki_repo_amr_smh.domain.models.LootItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel para LootScreen y LootDetailScreen.
 * Expone flows para la UI y funciones CRUD.
 */

data class LootUiState(
    val items: List<LootItem> = emptyList(),
    val isLoading: Boolean = false
)

class LootViewModel(private val repository: LootRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LootUiState())
    val uiState: StateFlow<LootUiState> = _uiState.asStateFlow()

    // control simple de diálogo de añadir
    private val _isAddDialogOpen = MutableStateFlow(false)
    val isAddDialogOpen: StateFlow<Boolean> = _isAddDialogOpen.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeAll().collect { list ->
                _uiState.update { it.copy(items = list) }
            }
        }
    }

    fun showAddDialog(show: Boolean) {
        _isAddDialogOpen.value = show
    }

    fun addItem(item: LootItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.add(item)
        }
    }

    fun updateItem(item: LootItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(item)
        }
    }

    fun deleteItem(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(id)
        }
    }

    fun toggleFavorite(item: LootItem) {
        val updated = item.copy(isFavorite = !item.isFavorite)
        updateItem(updated)
    }

    // Observa item por id (útil para pantalla detalle)
    fun observeById(id: Long): Flow<LootItem?> = repository.observeById(id)
}
