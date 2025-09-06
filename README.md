# ToDo App - Android Project

A modern Android ToDo application built with Kotlin, and Material Design 3. This app allows users to
manage their tasks efficiently with a clean and intuitive interface.

## Features

* **Task Management:**
    * Add new tasks with titles and descriptions.
    * View tasks organized by date.
    * Mark tasks as completed.
    * Edit existing tasks.
    * Delete tasks with a swipe gesture.
* **Calendar View:**
    * Interactive weekly calendar to navigate and select dates.
    * Displays tasks for the selected day.
    * Highlights the current selected day.
* **Localization:**
    * Supports multiple languages: English and Arabic.
    * App interface, dates, and numbers adapt to the selected language.
* **Theming:**
    * Supports Light and Dark themes.
    * The selected theme is applied throughout the app, including the splash screen.
    * Theme preference is saved and applied on app launch.
* **Settings Screen:**
    * Change the application language (English/Arabic).
    * Switch between Light and Dark themes.
* **Modern Android Practices:**
    * Built with 100% Kotlin.
    * Uses Android 12+ Splash Screen API.
    * Material Design 3 components and styling.

## Tech Stack & Libraries Used

* **Language:** Kotlin
* **Architecture:** MVVM (Model-View-ViewModel)
* **UI Framework:** XML with Material Components for Android (Material 3)
* **Navigation:** Android Navigation Component (Fragments)
* **Asynchronous Operations:** Kotlin Coroutines
* **Database:** Room Persistence Library for local data storage.
* **Calendar:** [Kizitonwose Calendar](https://github.com/kizitonwose/Calendar) for a customizable
  calendar view.
* **Shared Preferences:** For storing user settings (theme, language).
* **Splash Screen:** Android 12+ Core Splashscreen API.

## Screen Recording

<img src="https://github.com/Menna120/ToDo/blob/master/screen_record/todo_app_screen_recording.gif" width="300"/>

## Getting Started

1. Clone the repository:
   ```bash
   https://github.com/Menna120/ToDo.git
   ```
2. Open the project in Android Studio (latest stable version recommended).
3. Let Android Studio download Gradle dependencies and sync the project.
4. Build and run the app on an Android device or emulator.
