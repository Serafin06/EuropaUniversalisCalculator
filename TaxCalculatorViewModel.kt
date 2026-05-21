package pl.rafapp.europauniversaliscalculator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pl.rafapp.europauniversaliscalculator.data.TaxCalculatorRepository
import pl.rafapp.europauniversaliscalculator.data.UiState
import pl.rafapp.europauniversaliscalculator.data.CorruptionLevel

/**
 * ViewModel for handling tax income calculations.
 */
class TaxCalculatorViewModel(private val repository: TaxCalculatorRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> get() = _uiState

    fun calculateIncome(
        baseTax: Double,
        vassalTax: Double,
        emperorIncome: Double,
        regularUnits: Int,
        mercenaryUnits: Int,
        ships: Int,
        loans: Int,
        plagueMultiplier: Double
    ) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val income = repository.calculateIncome(
                    baseTax,
                    vassalTax,
                    emperorIncome,
                    regularUnits,
                    mercenaryUnits,
                    ships,
                    loans,
                    plagueMultiplier
                ).collect { state ->
                    when (state) {
                        is UiState.Success -> _uiState.value = state
                        is UiState.Error -> _uiState.value = state
                        UiState.Loading -> {}
                    }
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
