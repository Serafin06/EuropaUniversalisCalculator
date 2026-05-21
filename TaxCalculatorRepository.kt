package pl.rafapp.europauniversaliscalculator.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * Repository for calculating tax income.
 */
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

/**
 * Interface for calculating tax income.
 */
interface TaxCalculatorRepository {
    fun calculateIncome(
        baseTax: Double,
        vassalTax: Double,
        emperorIncome: Double,
        regularUnits: Int,
        mercenaryUnits: Int,
        ships: Int,
        loans: Int,
        plagueMultiplier: Double
    ): Flow<UiState>
}

/**
 * Implementation of TaxCalculatorRepository.
 */
class TaxCalculatorRepositoryImpl : TaxCalculatorRepository {
    private val _incomeFlow = MutableStateFlow<UiState>(Loading)
    override fun calculateIncome(
        baseTax: Double,
        vassalTax: Double,
        emperorIncome: Double,
        regularUnits: Int,
        mercenaryUnits: Int,
        ships: Int,
        loans: Int,
        plagueMultiplier: Double
    ): Flow<UiState> {
        return _incomeFlow
    }
}
