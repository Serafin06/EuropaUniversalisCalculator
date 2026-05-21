package pl.rafapp.europauniversaliscalculator.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.rafapp.europauniversaliscalculator.ui.theme.EuropaUniversalisCalculatorTheme
import pl.rafapp.europauniversaliscalculator.ui.viewModel.TaxCalculatorViewModel

private val BgColor     = Color(0xFF0F1923)
private val CardColor   = Color(0xFF1A2535)
private val AccentGold  = Color(0xFFE8B84B)
private val AccentRed   = Color(0xFFB84B4B)
private val TextColor   = Color(0xFFE0E0E0)
private val LabelColor  = Color(0xFF9E9E9E)
private val GreenColor  = Color(0xFF4CAF50)
private val BorderColor = Color(0xFF2E3F55)

@Composable
fun TaxCalculatorScreen(
    viewModel: TaxCalculatorViewModel = viewModel(factory = TaxCalculatorViewModel.Factory)
) {
    // Income
    var baseTax       by remember { mutableStateOf("") }
    var vassalTax     by remember { mutableStateOf("") }
    var emperorIncome by remember { mutableStateOf("") }
    var ideasIncome   by remember { mutableStateOf("") }
    var stabilityMod  by remember { mutableStateOf(0) } // -1=negative, 0=none, 1=positive

    // Costs
    var regularUnits    by remember { mutableStateOf("") }
    var mercenaryUnits  by remember { mutableStateOf("") }
    var ships           by remember { mutableStateOf("") }
    var loans           by remember { mutableStateOf("") }
    var plagueTaxIncome by remember { mutableStateOf("") }
    var advisor1        by remember { mutableStateOf("") }
    var advisor2        by remember { mutableStateOf("") }
    var advisor3        by remember { mutableStateOf("") }
    var isPapal         by remember { mutableStateOf(false) }

    // Monarch Power
    var rulerAdm  by remember { mutableStateOf("") }
    var rulerDip  by remember { mutableStateOf("") }
    var rulerMil  by remember { mutableStateOf("") }
    var advisorAdm by remember { mutableStateOf("") }
    var advisorDip by remember { mutableStateOf("") }
    var advisorMil by remember { mutableStateOf("") }
    var isEmperor  by remember { mutableStateOf(false) }

    // Section toggles
    var showPapalInfo   by remember { mutableStateOf(false) }
    var showEmperorInfo by remember { mutableStateOf(false) }
    var showBaseTaxInfo by remember { mutableStateOf(false) }

    // Dynamic calculation
    val advisorDiscount = if (isPapal) 1 else 0
    val totalIncome by remember {
        derivedStateOf {
            (baseTax.toDoubleOrNull() ?: 0.0) +
                    (vassalTax.toDoubleOrNull() ?: 0.0) +
                    (emperorIncome.toDoubleOrNull() ?: 0.0) +
                    (ideasIncome.toDoubleOrNull() ?: 0.0) +
                    when (stabilityMod) { 1 -> 2.0; -1 -> -2.0; else -> 0.0 }
        }
    }
    val totalCosts by remember {
        derivedStateOf {
            val adv = listOf(advisor1, advisor2, advisor3)
                .sumOf { maxOf(0, (it.toIntOrNull() ?: 0) - advisorDiscount).toDouble() }
            adv +
                    (regularUnits.toIntOrNull() ?: 0) * 1.0 +
                    (mercenaryUnits.toIntOrNull() ?: 0) * 2.0 +
                    (ships.toIntOrNull() ?: 0) * 0.5 +
                    (loans.toIntOrNull() ?: 0) * 1.0 +
                    (plagueTaxIncome.toDoubleOrNull() ?: 0.0) * 0.5
        }
    }
    val netIncome by remember { derivedStateOf { totalIncome - totalCosts } }

    val admPower by remember { derivedStateOf {
        (rulerAdm.toIntOrNull() ?: 0) + (advisorAdm.toIntOrNull() ?: 0) + if (isPapal) 1 else 0
    }}
    val dipPower by remember { derivedStateOf {
        (rulerDip.toIntOrNull() ?: 0) + (advisorDip.toIntOrNull() ?: 0)
    }}
    val milPower by remember { derivedStateOf {
        (rulerMil.toIntOrNull() ?: 0) + (advisorMil.toIntOrNull() ?: 0)
    }}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.AccountBalance, contentDescription = null, tint = AccentGold, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(10.dp))
            Column {
                Text("EU IV Calculator", color = AccentGold, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("Tax & Power", color = LabelColor, fontSize = 12.sp)
            }
        }

        // Live result bar
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = if (netIncome >= 0) Color(0xFF1A3525) else Color(0xFF351A1A)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LiveStat("Income", "+%.1f D".format(totalIncome), GreenColor)
                LiveStat("Costs", "-%.1f D".format(totalCosts), AccentRed)
                LiveStat("Net", "%.1f D".format(netIncome), if (netIncome >= 0) GreenColor else AccentRed)
            }
        }

        // INCOME CARD
        SectionCard("INCOME", Icons.Filled.TrendingUp, AccentGold) {
            // Base Tax with info toggle
            Row(verticalAlignment = Alignment.CenterVertically) {
                CalcRow(
                    label = "Base Tax",
                    value = baseTax,
                    onValueChange = { baseTax = it },
                    icon = Icons.Filled.Home,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { showBaseTaxInfo = !showBaseTaxInfo }) {
                    Icon(Icons.Filled.Info, contentDescription = null, tint = AccentGold, modifier = Modifier.size(18.dp))
                }
            }
            AnimatedVisibility(visible = showBaseTaxInfo) {
                InfoBox("Suma z Small Town Track + Large Town Track. Kluczowy przychód bazowy — rozwijaj miasta by go zwiększyć.")
            }

            CalcRow("Vassal Tax", vassalTax, { vassalTax = it }, Icons.Filled.Groups)
            CalcRow("Emperor Income", emperorIncome, { emperorIncome = it }, Icons.Filled.Star)
            CalcRow("Ideas Income", ideasIncome, { ideasIncome = it }, Icons.Filled.Lightbulb)

            // Stability modifier
            Text("Stability Modifier", color = LabelColor, fontSize = 13.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StabilityChip("-3s  (-2D)", stabilityMod == -1, AccentRed) { stabilityMod = if (stabilityMod == -1) 0 else -1 }
                StabilityChip("0", stabilityMod == 0, LabelColor) { stabilityMod = 0 }
                StabilityChip("+3s  (+2D)", stabilityMod == 1, GreenColor) { stabilityMod = if (stabilityMod == 1) 0 else 1 }
            }
        }

        // COSTS CARD
        SectionCard("COSTS", Icons.Filled.TrendingDown, AccentRed) {
            // Papal toggle with info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.ChurchIcon(), contentDescription = null, tint = AccentGold, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Papal Controller", color = TextColor, fontSize = 14.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { showPapalInfo = !showPapalInfo }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Filled.Info, contentDescription = null, tint = AccentGold, modifier = Modifier.size(16.dp))
                    }
                    Switch(
                        checked = isPapal,
                        onCheckedChange = { isPapal = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = AccentGold, checkedTrackColor = Color(0xFF5C4A1A))
                    )
                }
            }
            AnimatedVisibility(visible = showPapalInfo) {
                InfoBox("Papal Controller płaci 1D mniej za każdego Advisora.\nOtrzymuje też +1 Admin Monarch Power podczas kolekcji.\nZdobywa Prestige równy (liczba katolickich graczy − 1), max 3.")
            }

            Text("Advisors (1–4D each)", color = LabelColor, fontSize = 12.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = advisor1, onValueChange = { advisor1 = it },
                    label = { Text("Adv 1", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = inputColors()
                )
                OutlinedTextField(
                    value = advisor2, onValueChange = { advisor2 = it },
                    label = { Text("Adv 2", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = inputColors()
                )
                OutlinedTextField(
                    value = advisor3, onValueChange = { advisor3 = it },
                    label = { Text("Adv 3", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = inputColors()
                )
            }

            CalcRow("Regular Units (1D)", regularUnits, { regularUnits = it }, Icons.Filled.Shield, isInt = true)
            CalcRow("Mercenaries (2D)", mercenaryUnits, { mercenaryUnits = it }, Icons.Filled.PersonSearch, isInt = true)
            CalcRow("Ships at Sea (½D)", ships, { ships = it }, Icons.Filled.Sailing, isInt = true)
            CalcRow("Loans (1D each)", loans, { loans = it }, Icons.Filled.CreditCard, isInt = true)
            CalcRow("Plague Tax Income", plagueTaxIncome, { plagueTaxIncome = it }, Icons.Filled.Warning)
            if (plagueTaxIncome.toDoubleOrNull() ?: 0.0 > 0) {
                Text("  ↳ koszt plagi: -%.1f D".format((plagueTaxIncome.toDoubleOrNull() ?: 0.0) * 0.5),
                    color = AccentRed, fontSize = 12.sp)
            }
        }

        // MONARCH POWER CARD
        SectionCard("MONARCH POWER", Icons.Filled.Bolt, Color(0xFF7B9FE0)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.AccountBalance, contentDescription = null, tint = AccentGold, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("HRE Emperor", color = TextColor, fontSize = 14.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { showEmperorInfo = !showEmperorInfo }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Filled.Info, contentDescription = null, tint = AccentGold, modifier = Modifier.size(16.dp))
                    }
                    Switch(
                        checked = isEmperor,
                        onCheckedChange = { isEmperor = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = AccentGold, checkedTrackColor = Color(0xFF5C4A1A))
                    )
                }
            }
            AnimatedVisibility(visible = showEmperorInfo) {
                InfoBox("Emperor zbiera dodatkowe Monarch Power zależnie od wartości Elektorów (E).\nPrzy E=6 zdobywa 1 Prestige.\nWysoki E daje też bonus do dochodu podczas Collect Income.")
            }

            Text("Ruler Skills", color = LabelColor, fontSize = 12.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PowerField("ADM", rulerAdm, { rulerAdm = it }, Color(0xFF4B8BE8), Modifier.weight(1f))
                PowerField("DIP", rulerDip, { rulerDip = it }, Color(0xFF4BE8A0), Modifier.weight(1f))
                PowerField("MIL", rulerMil, { rulerMil = it }, Color(0xFFE84B4B), Modifier.weight(1f))
            }
            Text("Advisor Skills", color = LabelColor, fontSize = 12.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PowerField("ADM", advisorAdm, { advisorAdm = it }, Color(0xFF4B8BE8), Modifier.weight(1f))
                PowerField("DIP", advisorDip, { advisorDip = it }, Color(0xFF4BE8A0), Modifier.weight(1f))
                PowerField("MIL", advisorMil, { advisorMil = it }, Color(0xFFE84B4B), Modifier.weight(1f))
            }

            // po sekcji Monarch Power, przed Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    baseTax = ""; vassalTax = ""; emperorIncome = ""; ideasIncome = ""
                    stabilityMod = 0
                    regularUnits = ""; mercenaryUnits = ""; ships = ""; loans = ""
                    plagueTaxIncome = ""; advisor1 = ""; advisor2 = ""; advisor3 = ""
                    isPapal = false
                    rulerAdm = ""; rulerDip = ""; rulerMil = ""
                    advisorAdm = ""; advisorDip = ""; advisorMil = ""
                    isEmperor = false
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E1A1A)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = null, tint = AccentRed, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text("RESET ALL", color = AccentRed, fontWeight = FontWeight.Bold)
            }

            // Live power result
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PowerResult("ADM", admPower, Color(0xFF4B8BE8), isPapal)
                PowerResult("DIP", dipPower, Color(0xFF4BE8A0), false)
                PowerResult("MIL", milPower, Color(0xFFE84B4B), false)
            }
            if (isPapal) Text("  +1 ADM z Papal Controller", color = AccentGold, fontSize = 11.sp)
        }

        Spacer(Modifier.height(16.dp))
    }
}

// ── Helpers ──────────────────────────────────────────────────────────────────

@Composable
private fun SectionCard(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text(title, color = iconTint, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.sp)
            }
            content()
        }
    }
}

@Composable
private fun CalcRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    isInt: Boolean = false
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(icon, contentDescription = null, tint = LabelColor, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(label, color = LabelColor, fontSize = 13.sp)
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.width(90.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = if (isInt) KeyboardType.Number else KeyboardType.Decimal),
            colors = inputColors(),
            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, color = TextColor)
        )
    }
}

@Composable
private fun LiveStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(label, color = LabelColor, fontSize = 11.sp)
    }
}

@Composable
private fun StabilityChip(label: String, selected: Boolean, color: Color, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(6.dp),
        color = if (selected) color.copy(alpha = 0.25f) else CardColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) color else BorderColor),
        modifier = Modifier.height(32.dp)
    ) {
        Box(Modifier.padding(horizontal = 10.dp), contentAlignment = Alignment.Center) {
            Text(label, color = if (selected) color else LabelColor, fontSize = 12.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
        }
    }
}

@Composable
private fun InfoBox(text: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF1E2D1E),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text, color = Color(0xFF90C090), fontSize = 12.sp, modifier = Modifier.padding(10.dp), lineHeight = 18.sp)
    }
}

@Composable
private fun PowerField(label: String, value: String, onValueChange: (String) -> Unit, color: Color, modifier: Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 11.sp, color = color) },
        modifier = modifier,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextColor, unfocusedTextColor = TextColor,
            focusedBorderColor = color, unfocusedBorderColor = color.copy(alpha = 0.4f)
        )
    )
}

@Composable
private fun PowerResult(label: String, value: Int, color: Color, bonus: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("$value♦", color = color, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(label, color = LabelColor, fontSize = 11.sp)
    }
}

@Composable
private fun inputColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = TextColor, unfocusedTextColor = TextColor,
    focusedBorderColor = AccentGold, unfocusedBorderColor = BorderColor
)

// Workaround — Material Icons nie ma Church, używamy odpowiednika
private fun Icons.Filled.ChurchIcon() = Icons.Filled.AccountBalance

@Preview(showBackground = true, backgroundColor = 0xFF0F1923)
@Composable
fun TaxCalculatorScreenPreview() {
    EuropaUniversalisCalculatorTheme {
        TaxCalculatorScreen()
    }
}