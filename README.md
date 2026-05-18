# NIT3213 Final Assignment — Android App

## Author
- **Name:** Rahul
- **Student ID:** s8114019

## Tech Stack
- **Language:** Kotlin
- **Architecture:** MVVM, Single-Activity, Fragments
- **Libraries:** Retrofit, Moshi, Hilt (DI), Jetpack Navigation, Coroutines, Flow
- **UI:** View Binding, Material Components, RecyclerView

## Setup & Run
1. Open in Android Studio.
2. Sync Gradle files.
3. Run on an emulator/device (API 24+).

## Test Credentials
- **Username:** `s8114019`
- **Password:** `Rahul`
- **Campus:** Sydney

## API Details
- **Base URL:** `https://nit3213api.onrender.com/`
- **Auth:** `POST /{campus}/auth` -> returns `keypass`
- **Dashboard:** `GET /dashboard/{keypass}`

## Tests
Run `./gradlew test` to execute unit tests.
