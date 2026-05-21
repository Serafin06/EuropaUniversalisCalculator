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
        Card(elevation = CardDefaults.cardElevation()) {
            Text("Dochód", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(16.dp))
            TextField(
                value = viewModel.baseTax,
                onValueChange = { viewModel.onBaseTaxChange(it) },
                label = { Text("Base Tax") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = viewModel.vassalTax,
                onValueChange = { viewModel.onVassalTaxChange(it) },
                label = { Text("Vassal Tax") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = viewModel.emperorIncome,
                onValueChange = { viewModel.onEmperorIncomeChange(it) },
                label = { Text("Emperor Income") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Card(elevation = CardDefaults.cardElevation()) {
            Text("Koszty", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(16.dp))
            TextField(
                value = viewModel.regularUnits,
                onValueChange = { viewModel.onRegularUnitsChange(it) },
                label = { Text("Regular Units") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = viewModel.mercenaryUnits,
                onValueChange = { viewModel.onMercenaryUnitsChange(it) },
                label = { Text("Mercenary Units") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = viewModel.ships,
                onValueChange = { viewModel.onShipsChange(it) },
                label = { Text("Ships at Sea") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = viewModel.loans,
                onValueChange = { viewModel.onLoansChange(it) },
                label = { Text("Loans") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = viewModel.plagueTaxIncome,
                onValueChange = { viewModel.onPlagueTaxIncomeChange(it) },
                label = { Text("Plague Tax Income") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            onClick = { viewModel.calculateIncome() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Przelicz")
        }
    }
}
