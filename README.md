# NIT3213 Final Assignment — Android App

A simple three-screen Android application that authenticates against the NIT3213 API and displays a list of entities returned from the dashboard endpoint.

## Author

- **Name:** Rahul
- **Student ID:** s8114019
- **Class location:** Sydney (`/sydney/auth`)

## Features

1. **Login screen** — username + password fields, calls `POST /sydney/auth`, shows error messages on failure.
2. **Dashboard screen** — calls `GET /dashboard/{keypass}`, shows results in a RecyclerView (description hidden).
3. **Details screen** — shows all properties of the selected entity, including the description.

## Tech stack

- Kotlin, Coroutines, Kotlin Flow (`StateFlow`)
- MVVM with `ViewModel` (`androidx.lifecycle`)
- Retrofit 2 + Moshi for networking, OkHttp logging interceptor
- Hilt for dependency injection
- RecyclerView with `ListAdapter` + `DiffUtil`
- Material Components for UI
- View Binding
- JUnit 4 + Mockito-Kotlin + `kotlinx-coroutines-test` for unit tests

## Project structure

```
app/src/main/java/com/example/nit3213app/
├── NIT3213Application.kt          # @HiltAndroidApp entry point
├── data/
│   ├── api/
│   │   ├── ApiService.kt          # Retrofit interface
│   │   └── models/                # LoginRequest/Response, Entity, DashboardResponse
│   └── repository/
│       └── AppRepository.kt       # Single source of truth, @Singleton
├── di/
│   └── NetworkModule.kt           # Provides Moshi/OkHttp/Retrofit/ApiService
├── ui/
│   ├── login/                     # LoginActivity + LoginViewModel
│   ├── dashboard/                 # DashboardActivity + ViewModel + EntityAdapter
│   └── details/                   # DetailsActivity
└── util/
    └── Resource.kt                # Idle/Loading/Success/Error sealed wrapper

app/src/test/java/com/example/nit3213app/
├── MainCoroutineRule.kt           # JUnit rule for swapping Main dispatcher
├── LoginViewModelTest.kt
├── DashboardViewModelTest.kt
└── AppRepositoryTest.kt
```

## Build / run instructions

### Prerequisites

- Android Studio (Panda 4 or newer — Hedgehog and later all work)
- JDK 17 (bundled with recent Android Studio)
- Android SDK with Platform 34 installed
- An emulator (e.g. Pixel 8 API 34/35/36/37) or a physical device with API 24+

### Steps

1. Open the project root in Android Studio (`File → Open` → select the `NIT3213App` folder).
2. When prompted, choose **Use Gradle from: gradle-wrapper.properties file** and let Android Studio download Gradle 8.7 and regenerate the wrapper.
3. Let Gradle sync finish — it will download Hilt, Retrofit, Moshi, etc.
4. Pick a run target (Pixel 8 emulator is fine).
5. Press **Run** (Shift + F10).

> **Note on `gradle-wrapper.jar`:** the binary wrapper jar is intentionally not committed (per `.gitignore` conventions some teams follow). Android Studio will regenerate it on first sync. If you prefer running from the terminal, run `gradle wrapper --gradle-version 8.7` once with a system Gradle installation, then `./gradlew test` etc. will work.

### Test credentials

When the login screen appears, enter:

- **Username:** `Rahul`
- **Password:** `s8114019`

These match the NIT3213 API contract for the Sydney campus.

### Running unit tests

From the terminal in the project root:

```
./gradlew test
```

Or in Android Studio: right-click `app/src/test/java/com/example/nit3213app` → **Run 'Tests in nit3213app'**.

## API summary

| Endpoint                  | Method | Purpose             |
|---------------------------|--------|---------------------|
| `/sydney/auth`            | POST   | Authenticate user   |
| `/dashboard/{keypass}`    | GET    | Fetch entity list   |

Base URL: `https://nit3213api.onrender.com/`

## Switching campus

If you need to point the app at a different campus, change the `location` value in
`LoginViewModel.kt` from `"sydney"` to `"footscray"` or `"ort"`.

## Notes

- `ProgressBar` is shown while a network call is in flight.
- Error messages from the server are surfaced via the `Resource.Error` sealed branch.
- Adapter uses `DiffUtil` so list updates are efficient.
- Hilt provides the `AppRepository` to both ViewModels via constructor injection.
