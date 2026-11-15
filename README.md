# Emoji TCG

A native-feeling Android collectible emoji card battler built with Kotlin and Jetpack Compose.

## MVP
- Emoji character collection with rarity, HP, element, attack and skill stats
- Select an active card from your collection
- Offline bot arena with turn-based attacks and coin rewards
- Booster packs bought with earned in-game coins
- Shop and collection screens
- GitHub Actions builds a debug APK on every push to `main`

## Build
Open in Android Studio with JDK 17, or run `gradle assembleDebug`.
APK: `app/build/outputs/apk/debug/app-debug.apk`.

## Roadmap
Online multiplayer, accounts, cloud saves, deck construction and expanded battle rules are planned. The current bot battle and card economy run locally on-device.

Package: `com.emojitcg.game` • Minimum Android: 6.0 (API 23) • Version: 0.1.0
