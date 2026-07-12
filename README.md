# Twitch Android App

[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.0-purple.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-2026.06.00-green.svg?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Clean Architecture](https://img.shields.io/badge/Architecture-Clean-blue.svg?style=flat)](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

A modern, high-performance Android application built with the latest technologies and Clean Architecture principles. This project serves as a practice ground for implementing complex UI, robust networking, and scalable project structures inspired by the Twitch platform.

---

## Features

- Modern UI: Built entirely with Jetpack Compose for a reactive and declarative UI experience.
- Clean Architecture: Organized by features with decoupled layers for maximum testability and maintainability.
- Splash Screen API: Integrated with the official Android Core Splashscreen library including support for animated logos.
- Dynamic Theming: Custom Twitch-inspired theme with Material 3 support.
- Performance Optimized: Using the latest AGP 9.2.1 and Kotlin 2.4.0 for efficient builds and runtime performance.

---

## Tech Stack

- Language: Kotlin 2.4.0
- UI Framework: Jetpack Compose (Material 3)
- Dependency Injection: Hilt 2.59.2
- Networking: Retrofit 3.0.0 and OkHttp 5.4.0
- Image Loading: Coil 3.5.0
- Serialization: Kotlinx Serialization
- Asynchrony: Coroutines and Flow
- Logging: Timber 5.0.1
- Android SDK: Target 37, Min 29

---

## Project Structure

The project follows a feature-based modular structure within the app module:

```
com.madiwist.twitch/
├── core/               # Shared logic, DI, and common utilities
│   ├── di/             # Global dependency injection modules
│   ├── domain/         # Common domain models and interfaces
│   └── presentation/   # Design system (Theme, Color, Type) and components
├── feature_activity/   # Activity feed functionality
├── feature_auth/       # Authentication and user login flow
├── feature_chat/       # Real-time chat features
├── feature_post/       # Content creation and posting
├── feature_profile/    # User profile management
├── feature_search/     # Search and discovery
└── TwitchApplication.kt # Application entry point
```

---

## Design System

The application follows a sleek, dark aesthetic:
- Primary Color: #03B100 (Twitch-inspired Green)
- Background: #202020 (Dark Gray/Black)
- Surface: #3F3F3F (Medium Dark Gray)
- Text Primary: #FFFFFF (White)

---

## Getting Started

1. Clone the repository.
2. Open in Android Studio Meerkat or newer.
3. Sync Gradle and run on an emulator or physical device.

---

Made by MADI (https://madiwist.dev)
