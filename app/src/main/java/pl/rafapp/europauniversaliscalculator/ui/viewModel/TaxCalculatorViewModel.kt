package pl.rafapp.europauniversaliscalculator.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import pl.rafapp.europauniversaliscalculator.data.TaxCalculatorRepository
import pl.rafapp.europauniversaliscalculator.data.TaxCalculatorRepositoryImpl
import pl.rafapp.europauniversaliscalculator.data.UiState

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
        plagueTaxIncome: Double
    ) {
        viewModelScope.launch {
            repository.calculateIncome(
                baseTax, vassalTax, emperorIncome,
                regularUnits, mercenaryUnits, ships,
                loans, plagueTaxIncome
            )
                .catch { e -> _uiState.value = UiState.Error(e.message ?: "Unknown error") }
                .collect { state -> _uiState.value = state }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { TaxCalculatorViewModel(TaxCalculatorRepositoryImpl()) }
        }
    }
}