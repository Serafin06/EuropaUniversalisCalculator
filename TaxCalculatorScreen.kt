package pl.rafapp.europauniversaliscalculator.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import pl.rafapp.europauniversaliscalculator.data.UiState
import pl.rafapp.europauniversaliscalculator.ui.theme.EuropaUniversalisCalculatorTheme

class TaxCalculatorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EuropaUniversalisCalculatorTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    TaxCalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun TaxCalculatorScreen(viewModel: TaxCalculatorViewModel = ViewModelProvider(this).get(TaxCalculatorViewModel::class.java)) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Base Tax")
        // TODO: Add input field for base tax

        Text(text = "Vassal Tax")
        // TODO: Add input field for vassal tax

        Text(text = "Emperor's Income")
        // TODO: Add input field for emperor income

        Text(text = "Regular Units")
        // TODO: Add input field for regular units

        Text(text = "Mercenary Units")
        // TODO: Add input field for mercenary units

        Text(text = "Ships")
        // TODO: Add input field for ships

        Text(text = "Loans")
        // TODO: Add input field for loans

        Button(onClick = {
            // TODO: Get values from input fields and call viewModel.calculateIncome
        }) {
            Text("Calculate")
        }

        when (viewModel.uiState.value) {
            is UiState.Success -> {
                val income = (viewModel.uiState.value as UiState.Success).income
                val corruptionLevel = (viewModel.uiState.value as UiState.Success).corruptionLevel
                Text(text = "Income: $income")
                Text(text = "Corruption Level: ${corruptionLevel.value}")
            }
            is UiState.Error -> {
                val message = (viewModel.uiState.value as UiState.Error).message
                Text(text = "Error: $message")
            }
            UiState.Loading -> {
                Text(text = "Loading...")
            }
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
