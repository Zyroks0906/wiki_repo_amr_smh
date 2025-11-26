package com.amrsmh.wiki_repo_amr_smh.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amrsmh.wiki_repo_amr_smh.data.repository.MonsterRepository
import com.amrsmh.wiki_repo_amr_smh.di.ServiceLocator
import com.amrsmh.wiki_repo_amr_smh.domain.models.Monster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MonsterUiState(
    val monsters: List<Monster> = emptyList(),
    val isLoading: Boolean = false
)

class MonsterViewModel(private val repository: MonsterRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MonsterUiState())
    val uiState: StateFlow<MonsterUiState> = _uiState.asStateFlow()

    private val _isAddDialogOpen = MutableStateFlow(false)
    val isAddDialogOpen: StateFlow<Boolean> = _isAddDialogOpen.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeAll().collect { list ->
                _uiState.update { it.copy(monsters = list) }
            }
        }
    }

    fun showAddDialog(show: Boolean) {
        _isAddDialogOpen.value = show
    }

    fun addMonster(monster: Monster) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.add(monster)
        }
    }

    fun updateMonster(monster: Monster) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(monster)
        }
    }

    fun deleteMonster(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(id)
        }
    }

    fun toggleFavorite(monster: Monster) {
        val updated = monster.copy(isFavorite = !monster.isFavorite)
        updateMonster(updated)
    }

    fun observeById(id: Long): Flow<Monster?> = repository.observeById(id)
}

@Suppress("UNCHECKED_CAST")
class MonsterViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = ServiceLocator.provideMonsterRepository()
        return MonsterViewModel(repo) as T
    }
}