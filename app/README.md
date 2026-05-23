# 📱 Car Expense Tracker - Android App

A native Android application built with Kotlin that helps users track their vehicles, manage maintenance expenses, and monitor important document expiration dates (ITP/RCA).

## 🛠️ Tech Stack
* **Language:** Kotlin
* **UI/Layout:** XML, RecyclerView, Activities
* **Networking:** Retrofit2 & Gson (for asynchronous REST API calls)
* **Data Visualization:** MPAndroidChart (JitPack)
* **Architecture:** Standard Android MVC / MVVM concepts

## ✨ Key Features
* **Dynamic Analytics:** Visualizes expense distribution (Fuel, Repair, Insurance, etc.) using a beautiful, animated Pie Chart (`MPAndroidChart`).
* **Smart Alerts (Business Logic):** Automatically calculates days remaining until document expiration (ITP & RCA). Displays visual warnings (⚠️ Orange for <30 days, ❌ Red for expired).
* **Asynchronous Networking:** Handles background API requests safely using `Retrofit` callbacks to prevent UI thread blocking.
* **CRUD Interface:** Intuitive screens to add, edit, view, and delete vehicles and financial records.

## 🚀 Setup Instructions
1. Clone this repository.
2. Open the project in **Android Studio**.
3. **Important:** Ensure JitPack is in your `settings.gradle.kts`:
   ```kotlin
   dependencyResolutionManagement {
       repositories {
           google()
           mavenCentral()
           maven { url = uri("[https://jitpack.io](https://jitpack.io)") }
       }
   }