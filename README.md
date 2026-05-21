# Europa Universalis Calculator - Tax & Payout Calculator Module 🧮💶

A native Android application built with **Kotlin** and **Jetpack Compose**. This micro-app serves as an Economy Phase calculator for the board game *Europa Universalis: The Price of Power*. It will later be integrated into the broader **EUHelper** app.

---

## 🤖 AI Context & Instructions (For Aider / Continue)
> **Attention AI Assistant:** Read this section carefully before generating code.

### Core Architecture Guidelines:
1. **Language:** Kotlin.
2. **UI Toolkit:** Jetpack Compose.
3. **Architecture:** MVVM (Model-View-ViewModel).
    - `Model`: Data classes representing the game state (Income, Costs, Units).
    - `ViewModel`: Handles the mathematical logic for Phase B (Income minus Costs) and Phase C (Corruption).
    - `UI`: Reactive Compose screens that update immediately as parameters change.
4. **Current Scope:** No external database or backend is needed right now. Everything runs locally in memory.

---

## 🎲 Game Logic & Calculation Rules

The calculator must handle the following Economy Phase steps:

### Phase A: Cut Costs (UI Toggles / Adjustments)
The user adjusts their current state before calculation:
* Number of Advisors (and their individual upkeep: 1-4D).
* Number of Deployed Military Units (Regular, Mercenary).
* Number of Ships remaining at sea.

### Phase B: Collect Income minus Costs
The `ViewModel` must calculate the net Ducats (D) based on these inputs:

**INCOME (+):**
* **Base Tax Income:** Input from Town Tracks.
* **Vassal Tax Income:** Input from Vassal Track.
* **Emperor's E:** Equal to the Emperor value input.
* **Bureaucracy Idea:** Toggle (true/false) adding respective income.
* **Positive Stability (s):** Toggle, adds +2D.

**COSTS (-):**
* **Advisor Upkeep:** Sum of cost of all Advisors.
    * *Modifier:* If "Papal Controller" is true, subtract 1D per Advisor.
* **Military Maintenance:**
    * 1D per Regular Unit.
    * 2D per Mercenary Unit.
    * 0.5D (½D) per Ship remaining at sea.
* **Interest on Loans:** 1D per Loan.
* **Plague Effects:** Lose 0.5D (½D) per 1D of Tax Income from Areas with Plague.
* **Negative Stability (s):** Toggle, subtracts -2D.

### Phase C: Corruption
Calculate the cost of keeping Ducats in the Treasury after Phase B.
* **0–49 D:** No cost.
* **50–59 D:** 1a (Monarch Power) cost OR gain 1 Corruption.
* **60–69 D:** 2a cost OR gain Corruption per unpaid 'a'.
* **70–79 D:** 3a cost, etc.
* *Formula:* For every full 10D starting from 50D, the cost increases by 1a.

---

## 📁 Recommended Project Structure

```text
app/src/main/java/com/euhelper/calculator/
├── MainActivity.kt
├── model/
│   ├── EconomyState.kt       # Data classes for inputs
│   └── CalculationResult.kt  # Data classes for outputs
├── viewmodel/
│   └── EconomyViewModel.kt   # Core math and logic (Phases B & C)
└── ui/
    ├── screens/
    │   └── CalculatorScreen.kt # Main Compose UI
    └── components/           # Reusable sliders, toggles, and cards
```
--- 

## 🛠️ Getting Started
Open in Android Studio.

Sync Gradle files.

Run on an Android Emulator or physical device.
