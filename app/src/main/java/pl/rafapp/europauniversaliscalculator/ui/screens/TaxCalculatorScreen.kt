package pl.rafapp.europauniversaliscalculator.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.rafapp.europauniversaliscalculator.data.UiState
import pl.rafapp.europauniversaliscalculator.ui.theme.EuropaUniversalisCalculatorTheme
import pl.rafapp.europauniversaliscalculator.ui.viewModel.TaxCalculatorViewModel

@Composable
fun TaxCalculatorScreen(
    viewModel: TaxCalculatorViewModel = viewModel(factory = TaxCalculatorViewModel.Factory)
) {
    var baseTax by remember { mutableStateOf("") }
    var vassalTax by remember { mutableStateOf("") }
    var emperorIncome by remember { mutableStateOf("") }
    var regularUnits by remember { mutableStateOf("") }
    var mercenaryUnits by remember { mutableStateOf("") }
    var ships by remember { mutableStateOf("") }
    var loans by remember { mutableStateOf("") }
    var plagueTaxIncome by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = baseTax, onValueChange = { baseTax = it }, label = { Text("Base Tax") })
        TextField(value = vassalTax, onValueChange = { vassalTax = it }, label = { Text("Vassal Tax") })
        TextField(value = emperorIncome, onValueChange = { emperorIncome = it }, label = { Text("Emperor Income") })
        TextField(value = regularUnits, onValueChange = { regularUnits = it }, label = { Text("Regular Units") })
        TextField(value = mercenaryUnits, onValueChange = { mercenaryUnits = it }, label = { Text("Mercenary Units") })
        TextField(value = ships, onValueChange = { ships = it }, label = { Text("Ships at Sea") })
        TextField(value = loans, onValueChange = { loans = it }, label = { Text("Loans") })
        TextField(value = plagueTaxIncome, onValueChange = { plagueTaxIncome = it }, label = { Text("Plague Tax Income") })

        Button(onClick = {
            viewModel.calculateIncome(
                baseTax.toDoubleOrNull() ?: 0.0,
                vassalTax.toDoubleOrNull() ?: 0.0,
                emperorIncome.toDoubleOrNull() ?: 0.0,
                regularUnits.toIntOrNull() ?: 0,
                mercenaryUnits.toIntOrNull() ?: 0,
                ships.toIntOrNull() ?: 0,
                loans.toIntOrNull() ?: 0,
                plagueTaxIncome.toDoubleOrNull() ?: 0.0
            )
        }) {
            Text("Calculate")
        }

        when (val state = uiState) {
            is UiState.Success -> {
                Text(text = "Income: ${state.income}")
                Text(text = "Corruption Level: ${state.corruptionLevel.value}")
            }
            is UiState.Error -> Text(text = "Error: ${state.message}")
            UiState.Loading -> {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaxCalculatorScreenPreview() {
    EuropaUniversalisCalculatorTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            TaxCalculatorScreen()
        }
    }
}