package pl.rafapp.europauniversaliscalculator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    val bgColor = Color(0xFF1A1A2E)
    val cardColor = Color(0xFF16213E)
    val accentColor = Color(0xFFE8B84B)
    val textColor = Color(0xFFE0E0E0)
    val labelColor = Color(0xFF9E9E9E)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        Text(
            text = "EU Tax Calculator",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Income section
        SectionCard(title = "INCOME", cardColor = cardColor, accentColor = accentColor) {
            CalcRow("Base Tax", baseTax, { baseTax = it }, textColor, labelColor)
            CalcRow("Vassal Tax", vassalTax, { vassalTax = it }, textColor, labelColor)
            CalcRow("Emperor Income", emperorIncome, { emperorIncome = it }, textColor, labelColor)
        }

        // Costs section
        SectionCard(title = "COSTS", cardColor = cardColor, accentColor = accentColor) {
            CalcRow("Regular Units (1D)", regularUnits, { regularUnits = it }, textColor, labelColor, isInt = true)
            CalcRow("Mercenaries (2D)", mercenaryUnits, { mercenaryUnits = it }, textColor, labelColor, isInt = true)
            CalcRow("Ships at Sea (½D)", ships, { ships = it }, textColor, labelColor, isInt = true)
            CalcRow("Loans (1D)", loans, { loans = it }, textColor, labelColor, isInt = true)
            CalcRow("Plague Tax Income", plagueTaxIncome, { plagueTaxIncome = it }, textColor, labelColor)
        }

        // Calculate button
        Button(
            onClick = {
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
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("CALCULATE", color = Color(0xFF1A1A2E), fontWeight = FontWeight.Bold)
        }

        // Result
        when (val state = uiState) {
            is UiState.Success -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("RESULT", color = accentColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Net Income", color = labelColor)
                            Text("%.1f D".format(state.income), color = textColor, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Corruption", color = labelColor)
                            Text("Level ${state.corruptionLevel.value}", color = accentColor, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            is UiState.Error -> Text("Error: ${state.message}", color = Color.Red)
            UiState.Loading -> {}
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    cardColor: Color,
    accentColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, color = accentColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            content()
        }
    }
}

@Composable
private fun CalcRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    textColor: Color,
    labelColor: Color,
    isInt: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = labelColor, fontSize = 14.sp, modifier = Modifier.weight(1f))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.width(100.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = if (isInt) KeyboardType.Number else KeyboardType.Decimal),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedBorderColor = Color(0xFFE8B84B),
                unfocusedBorderColor = Color(0xFF444466)
            ),
            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TaxCalculatorScreenPreview() {
    EuropaUniversalisCalculatorTheme {
        TaxCalculatorScreen()
    }
}