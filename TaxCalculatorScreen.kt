@Composable
fun TaxCalculatorScreen(
    viewModel: TaxCalculatorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // HRE Controller Section
        Card(elevation = CardDefaults.cardElevation()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "HRE Controller", style = MaterialTheme.typography.titleSmall)
                Text(text = "Pay 1D fewer per Advisor", style = MaterialTheme.typography.body2)
            }
        }

        // Papal Discounts Section
        Card(elevation = CardDefaults.cardElevation()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "Papal Controller", style = MaterialTheme.typography.titleSmall)
                Text(text = "Pay 1D fewer per Advisor", style = MaterialTheme.typography.body2)
            }
        }

        // Income Sources Section
        Card(elevation = CardDefaults.cardElevation()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "Income Sources", style = MaterialTheme.typography.titleSmall)
                CalcRow(label = "Base Tax Income", value = uiState.income.toString())
                CalcRow(label = "Vassal Tax Income", value = uiState.income.toString())
                CalcRow(label = "Emperor's Income", value = uiState.income.toString())
            }
        }

        // Costs Section
        Card(elevation = CardDefaults.cardElevation()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "Costs", style = MaterialTheme.typography.titleSmall)
                CalcRow(label = "Advisor Upkeep", value = uiState.income.toString())
                CalcRow(label = "Military Maintenance", value = uiState.income.toString())
                CalcRow(label = "Interest on Loans", value = uiState.income.toString())
                CalcRow(label = "Plague Effects", value = uiState.income.toString())
            }
        }

        // Source Income/Cost Section
        Card(elevation = CardDefaults.cardElevation()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "Source Income/Cost", style = MaterialTheme.typography.titleSmall)
                CalcRow(label = "Town Tracks (Small and Large Town Track combined)", value = uiState.income.toString())
                CalcRow(label = "Vassal Track", value = uiState.income.toString())
            }
        }

        // Ideas Section
        Card(elevation = CardDefaults.cardElevation()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "Ideas", style = MaterialTheme.typography.titleSmall)
                CalcRow(label = "Income for Ideas", value = uiState.income.toString())
            }
        }

        // Display Income and Corruption Level
        when (uiState) {
            is UiState.Success -> {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "Income: ${uiState.income}", style = MaterialTheme.typography.body2)
                    Text(text = "Corruption Level: ${uiState.corruptionLevel.value}", style = MaterialTheme.typography.body2)
                }
            }
            UiState.Loading -> {
                CircularProgressIndicator()
            }
            is UiState.Error -> {
                Text(text = uiState.message, color = Color.Red)
            }
        }
    }
}

@Composable
private fun CalcRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.body2)
        Text(text = value, style = MaterialTheme.typography.body2)
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
