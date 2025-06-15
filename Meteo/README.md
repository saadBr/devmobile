# ☀️ Meteo App – Android Weather Forecast

**Meteo** is a modern Android application that displays 3-hour interval weather forecasts using data from the [OpenWeatherMap API](https://openweathermap.org/forecast5). Built entirely with **Jetpack Compose**, the app is optimized for both **smartphones** and **Wear OS smartwatches**.

---

## 🌍 Features

- 🔎 **City Search**: Enter a city name to fetch live weather forecasts.
- 🌡️ **Weather Details**:
    - Date & time
    - Minimum and maximum temperatures
    - Pressure (hPa)
    - Humidity (%)
    - Weather icon (loaded dynamically using Coil)
- 🎨 **Material 3 UI**: Styled with custom light and dark themes using Jetpack Compose.
- ⌚ **Wear OS Support**: Lightweight version adapted for circular screens using `ScalingLazyColumn` and `TimeText`.

---

## 🛠️ Tech Stack

- **Kotlin** – Primary programming language
- **Jetpack Compose** – UI toolkit for declarative UI
- **Material 3** – Latest Material Design components
- **ViewModel** – Lifecycle-aware data holder
- **Coroutines** – Async API calls
- **Coil** – Image loading library for Compose
- **OpenWeatherMap API** – Weather data provider

---

## 📦 API Key

This project uses OpenWeatherMap’s free API. You must insert your own API key in `WeatherViewModel.kt`:

```kotlin
private val apiKey = "YOUR_API_KEY_HERE"
```
Get your key from: https://openweathermap.org/api

## 🧪 Requirements
- Min SDK: 26 (required for Wear Compose)
- Target SDK: 34
- Kotlin: 1.9.23+
- Compose Compiler: Compatible version based on Kotlin

