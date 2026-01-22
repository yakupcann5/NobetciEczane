# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run all unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run a specific test class
./gradlew testDebugUnitTest --tests "com.yakupcan.nobetcieczane.ExampleUnitTest"

# Clean build
./gradlew clean

# Clean and rebuild
./gradlew clean assembleDebug

# Check for lint issues
./gradlew lint
```

## Architecture

This is a **Clean Architecture + MVVM** Android application for displaying pharmacies on duty in Turkey.

### Layer Structure

```
ui/                     # Presentation Layer
├── MainActivity.kt     # Single activity, handles theme/language config
├── splash/            # SplashFragment + ViewModel
├── maps/              # MapsFragment + ViewModel (Google Maps display)
├── listfragment/      # ListFragment + ViewModel (RecyclerView list)
├── filterfragment/    # FilterFragment + ViewModel (city/district filter)
├── settingsfragment/  # SettingsFragment + ViewModel (language/theme)
└── push/              # PushFragment + ViewModel (notifications)

domain/                # Domain Layer
├── repository/        # Repository interfaces (PharmacyRepository, FirebaseFirestoreRepository)
├── use_case/          # Business logic (GetPharmacyUseCase, FirebaseTokenSave, etc.)
└── model/             # Domain models (Pharmacy, Marker, Info, NowLocation)

data/                  # Data Layer
├── repository/        # Repository implementations (RepositoryImpl, FirestoreRepositoryImpl)
├── service/           # Retrofit service interfaces
└── model/             # DTOs (PharmacyResponse, DataDto, PushModel)

di/                    # Dependency Injection
├── AppModule.kt       # Retrofit, repositories, use cases
└── SharedModule.kt    # SharedPreferences

common/                # Utilities
├── Constants.kt       # API URLs, keys
└── RequestState.kt    # Sealed class for Loading/Success/Error states

util/                  # Helper classes
├── MyPreferences.kt           # SharedPreferences wrapper
├── InterstitialAdManager.kt   # Interstitial ad management
└── GPFirebaseMessagingService.kt  # FCM service
```

### Data Flow Pattern

```
Fragment → ViewModel → UseCase → Repository → Retrofit Service
                ↓
         StateFlow<RequestState<T>>
                ↓
         UI observes and renders
```

ViewModels expose `StateFlow<RequestState<T>>` where `RequestState` is a sealed class with `Loading`, `Success`, and `Error` variants.

### Key Technologies

- **SDK**: compileSdk 34, targetSdk 34, minSdk 24, Java 17
- **DI**: Hilt 2.47 - ViewModels use `@HiltViewModel`, Activities/Fragments use `@AndroidEntryPoint`
- **Async**: Kotlin Coroutines + Flow
- **Network**: Retrofit 2.9.0 with Gson converter
- **Maps**: Google Maps SDK + Play Services Location
- **Firebase**: FCM (push notifications), Firestore (token storage), Crashlytics, Analytics
- **Navigation**: Navigation Component 2.7.0 with Safe Args
- **Ads**: Google Mobile Ads 22.2.0 - Interstitial ads shown on filter save
- **View Binding**: Enabled for all layouts

### External APIs

1. **Pharmacy API**: `https://www.nosyapi.com/apiv2/pharmacyLink` - Returns pharmacy on duty data
2. **Firebase FCM**: `https://fcm.googleapis.com/` - Push notification delivery

### Navigation Graph

Entry point is `splashFragment`. Main screens: `mapsFragment2`, `listFragment2`, `filterFragment`, `settingsFragment`, `pushFragment`. Navigation defined in `res/navigation/nav.xml`.

### Local Storage

`MyPreferences` class wraps SharedPreferences for storing: selected city/district, theme preference, language setting, FCM tokens, cached pharmacy data.

### Ad Implementation

Interstitial ads are managed via `InterstitialAdManager` singleton. Ads are preloaded in `FilterFragment.initAds()` and shown when user saves filter selection. Banner ads were removed due to Google Play policy.
