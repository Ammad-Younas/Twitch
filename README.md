# 🎮 Twitch Android App

[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.0-purple.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-2026.06.00-green.svg?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Clean Architecture](https://img.shields.io/badge/Architecture-Clean-blue.svg?style=flat)](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

A modern, high-performance Android application built with the latest technologies and **Clean Architecture** principles. This project serves as a practice ground for implementing complex UI, robust networking, and scalable project structures.

---

## Features

- **Modern UI**: Built entirely with Jetpack Compose for a reactive and declarative UI experience.
- **Clean Architecture**: Decoupled layers (Data, Domain, Presentation) for maximum testability and maintainability.
- **Dynamic Theming**: Custom Twitch-inspired dark theme with Material 3 support.
- **Performance Optimized**: Using the latest AGP 9.0+ built-in Kotlin support and KSP for annotation processing.

---

## Tech Stack

- **Language**: Kotlin 2.4.0
- **UI Framework**: Jetpack Compose (Material 3)
- **Asynchrony**: Coroutines & Flow
- **Dependency Injection**: Hilt
- **Networking**: Retrofit & OkHttp
- **Serialization**: Kotlinx Serialization
- **Image Loading**: Coil 3.0
- **Logging**: Timber

---

## Project Structure

```
com.madiwist.twitch/
├── data/           # Repositories & Data Sources
├── domain/         # Business Logic & Use Cases
├── presentation/   # UI Layer (Screens, ViewModels, Theme)
│   ├── login/      # Authentication Flow
│   ├── splash/     # App Entry
│   └── ui/         # Design System (Color, Shape, Theme)
└── MainActivity.kt # Entry Activity
```

---

## Design System

The app follows a sleek, dark aesthetic:
- **Primary Color**: `#03B100` (Twitch-inspired Green)
- **Background**: `#000000` (Pure Black)
- **Surface**: `#3F3F3F` (Dark Gray)

---

## Getting Started

1. Clone the repository.
2. Open in **Android Studio Meerkat** or newer.
3. Sync Gradle and run on an emulator or physical device.

---

Made with ❤️ by [MADI](https://madiwist.dev)
