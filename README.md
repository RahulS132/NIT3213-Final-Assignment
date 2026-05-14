# NIT3213 Final Assignment — Android App

A three-screen Android application that authenticates against the NIT3213 API and displays a list of entities returned from the dashboard endpoint.

## Author

- **Name:** Rahul
- **Student ID:** s8114019

## Architecture

Follows the architecture diagram from the Week 3-4 notes:

```
MainActivity (single Activity host)
   └── NavHostFragment
         ├── LoginFragment      ──► LoginViewModel ──┐
         ├── DashboardFragment  ──► DashboardViewModel ─┤
         └── DetailsFragment                              │
                                                          ▼
                                                  AppRepository (Singleton)
                                                          │
                                                          ▼
                                                    ApiService (Retrofit)
                                                          │
                                                          ▼
                                                       Server
```

- **Activity** — `MainActivity` is the single host. It only inflates a `FragmentContainerView` running the Navigation Component graph.
- **Fragments** — one Fragment per screen (`LoginFragment`, `DashboardFragment`, `DetailsFragment`), navigated with `findNavController().navigate(actionId, bundle)`.
- **ViewModels** — one ViewModel per Fragment (`LoginViewModel`, `DashboardViewModel`), each exposing a `StateFlow<Resource<…>>`.
- **Repository** — `AppRepository` is the single source of truth. Both ViewModels depend on it.
- **Network layer** — `ApiService` is a Retrofit interface; `NetworkModule` (Hilt) provides Moshi, OkHttp (with logging + explicit timeouts), and Retrofit as `@Singleton`s.

## Tech stack

- Kotlin, Coroutines, Kotlin Flow (`StateFlow`)
- Single-Activity + Fragments + Navigation Component
- MVVM with `ViewModel` (`androidx.lifecycle`)
- Retrofit 2 + Moshi + OkHttp logging interceptor
- Hilt for dependency injection (`@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel`, `@Module`/`@Provides`)
- RecyclerView with `ListAdapter` + `DiffUtil`
- Material Components for UI (TextInputLayout, MaterialButton, exposed dropdown)
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
│   ├── main/MainActivity.kt       # Single host activity
│   ├── login/                     # LoginFragment + LoginViewModel
│   ├── dashboard/                 # DashboardFragment + ViewModel + EntityAdapter
│   └── details/                   # DetailsFragment
└── util/
    └── Resource.kt                # Idle/Loading/Success/Error sealed wrapper

app/src/main/res/
├── layout/
│   ├── activity_main.xml          # FragmentContainerView host
│   ├── fragment_login.xml
│   ├── fragment_dashboard.xml
│   ├── fragment_details.xml
│   └── item_entity.xml
└── navigation/
    └── nav_graph.xml              # login → dashboard → details

app/src/test/java/com/example/nit3213app/
├── MainCoroutineRule.kt
├── LoginViewModelTest.kt
├── DashboardViewModelTest.kt
└── AppRepositoryTest.kt
```

## Build / run instructions

### Prerequisites

- Android Studio (Panda 4 or newer)
- JDK 17 (bundled with recent Android Studio)
- Android SDK with Platform 34 installed
- An emulator (e.g. Pixel 8 API 34+) or a physical device with API 24+

### Steps

1. `File → Open` the `NIT3213App` folder.
2. Let Gradle sync — it will fetch Hilt, Retrofit, Moshi, and the Navigation Component.
3. Pick a run target and press **Run** (Shift + F10).

### Test credentials

- **Username:** `Rahul`
- **Password:** `s8114019`
- **Campus:** pick from the dropdown (Sydney / Footscray / ORT)

### Running unit tests

```
./gradlew test
```

Or in Android Studio: right-click `app/src/test/java/com/example/nit3213app` → **Run 'Tests in nit3213app'**.

## API contract

| Endpoint                  | Method | Purpose             |
|---------------------------|--------|---------------------|
| `/{campus}/auth`          | POST   | Authenticate user. Campus is one of `sydney`, `footscray`, `ort` |
| `/dashboard/{keypass}`    | GET    | Fetch entity list   |

Base URL: `https://nit3213api.onrender.com/`

Request body for `/{campus}/auth`:

```json
{ "username": "Rahul", "password": "s8114019" }
```

Successful response:

```json
{ "keypass": "topicName" }
```

## Troubleshooting

- **404 from `/{campus}/auth`** — that campus isn't deployed on the server right now. Use the campus dropdown to switch to another campus. If none of the three work, the API may be down — check with the tutor.
- **Spinner stays forever** — render.com free tier cold-starts can take 30–60 s on the first request. OkHttp is configured with a 90-second call timeout so it will surface a real error if the call hangs longer than that.
- **Network errors** — make sure the emulator has internet (open Chrome on the emulator and try a known site).

## Notes

- View Binding is enabled, so layouts are accessed as e.g. `FragmentLoginBinding`.
- Hilt provides the `AppRepository` to both ViewModels via constructor injection.
- All network state is wrapped in a `Resource` sealed class (`Idle / Loading / Success<T> / Error`).
- The RecyclerView uses `ListAdapter` + `DiffUtil` so list updates are efficient.
