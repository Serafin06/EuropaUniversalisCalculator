# Europa Universalis Calculator 🎮

A native Android application built in **Kotlin + Jetpack Compose** to help **Europa Universalis (board game)** players quickly calculate Tax Income, Costs and Monarch Power during gameplay.

## 📱 Features

- **Live Tax Calculation**: Income, Costs and Net update in real-time as you type
- **Income Tracking**: Base Tax, Vassal Tax, Emperor Income, Ideas Income
- **Cost Tracking**: Advisors, Regular Units, Mercenaries, Ships, Loans, Plague
- **Monarch Power**: ADM / DIP / MIL calculation from Ruler + Advisor skills
- **Papal Controller**: Toggle with automatic -1D discount per Advisor and +1 ADM bonus
- **HRE Emperor**: Toggle with rules reminder for E bonuses
- **Stability Modifier**: Quick chip selector for -3s / 0 / +3s
- **Plague Cost**: Auto-calculates ½D loss per 1D of affected Tax Income
- **In-app Rule Reminders**: Expandable info boxes for Papal, Emperor and Base Tax rules
- **Reset**: One-tap reset of all fields
- **Dark Theme**: EU-themed dark UI (navy & gold)

## 🎯 Perfect for EU Board Game Players Who Want To:

- Calculate net income instantly without pen and paper
- Avoid mistakes with Advisor discounts and plague penalties
- Track Monarch Power across all three types simultaneously
- Get quick rule reminders mid-game without opening the rulebook

## 🛠️ Technical Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Architecture**: MVVM (ViewModel + Repository)
- **State**: StateFlow + derivedStateOf for live updates
- **Min SDK**: 26

## 📁 Project Structure

```
app/src/main/java/pl/rafapp/europauniversaliscalculator/
├── data/
│   ├── TaxCalculatorRepository.kt   # Interface + Impl with calculation logic
│   └── UiState.kt                   # Sealed UiState + CorruptionLevel
├── ui/
│   ├── screens/
│   │   └── TaxCalculatorScreen.kt   # Main Composable screen
│   ├── viewModel/
│   │   └── TaxCalculatorViewModel.kt # ViewModel with Factory
│   └── theme/
│       └── EuropaUniversalisCalculatorTheme.kt
└── MainActivity.kt                  # Entry point
```

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or newer
- Android SDK 26+
- Kotlin 1.9+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Serafin06/europa-universalis-calculator.git
   cd europa-universalis-calculator
   ```

2. **Open in Android Studio**
    - File → Open → select the project folder

3. **Run the app**
    - Select a device or emulator (API 26+)
    - Press ▶ Run

### Building for Release

```bash
# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease
```

## 📋 How to Use

1. **Income section** — enter Base Tax (Small + Large Town Track combined), Vassal Tax, Emperor and Ideas income
2. **Stability** — tap the chip matching your current stability modifier
3. **Costs section** — fill in Advisor costs (1–4D each), unit counts, loans and plague income
4. **Papal Controller** — toggle on if you control the Papacy (auto-applies discount)
5. **Monarch Power** — enter Ruler and Advisor skill values to see ADM/DIP/MIL totals
6. **Live bar** at the top shows Income / Costs / Net instantly
7. **Reset** — tap the reset button to clear all fields for the next round

## 🧮 Calculation Logic

| Source | Formula |
|---|---|
| Base Tax | Town Track Small + Large |
| Vassal Tax | Vassal Track value |
| Stability bonus | +3s → +2D, -3s → -2D |
| Advisor upkeep | 1–4D per Advisor (Papal: -1D each) |
| Regular Units | 1D per unit |
| Mercenaries | 2D per unit |
| Ships at Sea | ½D per ship |
| Loans | 1D per loan |
| Plague | ½D × Tax Income from plague areas |

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Areas for Contribution

- [ ] Corruption cost calculator (D thresholds → Admin cost)
- [ ] Prestige scoring (Papal / Emperor / Absolute Monarchy)
- [ ] Save/load game state between sessions
- [ ] Multiple player tracking
- [ ] Unit tests for TaxCalculatorRepository
- [ ] English / Polish language toggle

## 🐛 Bug Reports

Found a bug? Please create an issue with:

- Device info (Android version, manufacturer)
- Steps to reproduce
- Expected vs actual behaviour
- Screenshot if applicable

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Aegir Games** for creating the Europa Universalis board game
- **Paradox Interactive** for the original EU4 video game
- **JetBrains / Google** for Kotlin and Jetpack Compose
- EU board game community for rules clarifications and feedback

## 📞 Contact

- **GitHub**: [@Serafin06](https://github.com/Serafin06)
- **Issues**: [GitHub Issues](https://github.com/Serafin06/europa-universalis-calculator/issues)

---

**Happy conquering, and may your treasury never run dry!** 🏰⚔️

> *"He who controls the taxes, controls the empire."*