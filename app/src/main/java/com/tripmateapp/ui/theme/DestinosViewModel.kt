package com.tripmateapp.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tripmateapp.BaseDatos.Destinos.DestinoEntity
import com.tripmateapp.repositorios.DestinoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// IMPORTA TU ENTITY Y REPOSITORY
// import com.tripmateapp.BaseDatos.Destinos.DestinoEntity
// import com.tripmateapp.data.repository.DestinoRepository

// ------------------------------
// UI STATE
// ------------------------------
data class DestinosUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val destinos: List<DestinoEntity> = emptyList()
)

// ------------------------------
// VIEWMODEL
// ------------------------------
class DestinosViewModel(
    private val repo: DestinoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DestinosUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        loadDestinos()
    }

    fun loadDestinos() {
        viewModelScope.launch {
            repo.getDestinos().collect { lista ->
                _uiState.update {
                    it.copy(
                        destinos = lista,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onSearchChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }

        viewModelScope.launch {
            repo.searchDestinos(query).collect { lista ->
                _uiState.update {
                    it.copy(destinos = lista)
                }
            }
        }
    }
}
