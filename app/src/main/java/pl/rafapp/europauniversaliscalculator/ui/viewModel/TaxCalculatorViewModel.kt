import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
            try {
                val income = repository.calculateIncome(
                    baseTax,
                    vassalTax,
                    emperorIncome,
                    regularUnits,
                    mercenaryUnits,
                    ships,
                    loans,
                    plagueTaxIncome
                )
                _uiState.value = UiState.Success(income, CorruptionLevel.Low)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "An error occurred")
            }
        }
    }
}

sealed class UiState {
    data class Success(val income: Double, val corruptionLevel: CorruptionLevel) : UiState()
    object Loading : UiState()
    data class Error(val message: String) : UiState()
}

sealed class CorruptionLevel(val value: Int) {
    object Low : CorruptionLevel(0)
    object Medium : CorruptionLevel(1)
    object High : CorruptionLevel(2)
    object VeryHigh : CorruptionLevel(3)
}
