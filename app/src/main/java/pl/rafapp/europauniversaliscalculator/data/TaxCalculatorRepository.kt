package pl.rafapp.europauniversaliscalculator.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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

interface TaxCalculatorRepository {
    fun calculateIncome(
        baseTax: Double,
        vassalTax: Double,
        emperorIncome: Double,
        regularUnits: Int,
        mercenaryUnits: Int,
        ships: Int,
        loans: Int,
        plagueTaxIncome: Double
    ): Flow<UiState>
}

class TaxCalculatorRepositoryImpl : TaxCalculatorRepository {
    override fun calculateIncome(
        baseTax: Double,
        vassalTax: Double,
        emperorIncome: Double,
        regularUnits: Int,
        mercenaryUnits: Int,
        ships: Int,
        loans: Int,
        plagueTaxIncome: Double
    ): Flow<UiState> = flow {
        emit(UiState.Loading)
        val income = baseTax + vassalTax + emperorIncome -
                (regularUnits * 1.0) -
                (mercenaryUnits * 2.0) -
                (ships * 0.5) -
                (loans * 1.0) -
                (plagueTaxIncome * 0.5)
        val corruption = when {
            income < 50 -> CorruptionLevel.Low
            income < 60 -> CorruptionLevel.Medium
            income < 70 -> CorruptionLevel.High
            else        -> CorruptionLevel.VeryHigh
        }
        emit(UiState.Success(income, corruption))
    }
}