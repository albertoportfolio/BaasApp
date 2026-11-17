# BaasAuth

A simple Android app built with **Kotlin** and **Jetpack Compose** that demonstrates:

- **State** management and **state hoisting**
- Authentication with a **BaaS** (Firebase Authentication)
- **Precondition functions** and exception handling
- A **custom, extensible exception**

## Requirements

- Android Studio (recent version)
- JDK 11
- minSdk 24 / compileSdk 35
- A Google account to create the Firebase project

## Features

### 1. State and State Hoisting

A single button increments two counters:

| Counter | Implementation | Behaviour on screen rotation |
|---------|----------------|------------------------------|
| Counter 1 | `rememberSaveable` | Keeps its value |
| Counter 2 | `remember` | Resets to 0 |

State is hoisted into `HomeScreen`. The `CounterSection` composable is stateless: it only receives the values and the `onIncrement` event from its parent.

### 2. BaaS: Firebase Authentication

Email and password authentication:

- User registration
- Sign in
- Sign out
- Error messages when sign in or registration fails

### 3. Exceptions with precondition functions

| Scenario | Function | Exception thrown |
|----------|----------|------------------|
| The light is not turned on | `check()` | `IllegalStateException` |
| A number must not be negative | `require()` | `IllegalArgumentException` |
| The input is not a valid number | `requireNotNull()` | `IllegalArgumentException` |

All exceptions are caught with `try/catch` (`validateLight`, `validateNumber`) and the message is shown on screen.

### 4. Custom exception

`WordTooLongException` is thrown when a word is longer than 10 characters.

- It is an `open` class, so it can be extended.
- `UsernameTooLongException` is an example subclass.
- A `catch (e: WordTooLongException)` also catches its subclasses.

## Project structure

```
app/src/main/java/com/example/baasauth/
├── MainActivity.kt   # UI, authentication, counters and exceptions demo
└── Exceptions.kt     # Preconditions and custom exception
```

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/<your-username>/<your-repository>.git
```

### 2. Configure Firebase

1. Go to the [Firebase console](https://console.firebase.google.com) and create a project.
2. Add an Android app with the package name `com.example.baasauth`.
3. Download `google-services.json` and place it in the `app/` folder.
4. Go to **Authentication → Sign-in method** and enable **Email/Password**.

> `google-services.json` contains your project's data. If the repository is public, consider adding it to `.gitignore`.

### 3. Gradle dependencies

`build.gradle.kts` (project root):

```kotlin
plugins {
    id("com.google.gms.google-services") version "4.4.2" apply false
}
```

`app/build.gradle.kts`:

```kotlin
plugins {
    id("com.google.gms.google-services")
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-auth")
}
```

`AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### 4. Run

Sync Gradle (**File → Sync Project with Gradle Files**) and run the app on an emulator or device.

## Known issues

**Kotlin metadata or `compileSdk` errors**

Some recent AndroidX libraries require AGP 9.1+, `compileSdk` 37 and Kotlin 2.4. This project uses AGP 8.10.0 and Kotlin 2.0.21, so it is best to pin compatible versions in `gradle/libs.versions.toml`:

```toml
coreKtx = "1.15.0"
lifecycleRuntimeKtx = "2.8.7"
activityCompose = "1.10.1"
composeBom = "2025.05.00"
```

If a transitive dependency keeps pulling in newer versions, you can force them in `app/build.gradle.kts`:

```kotlin
configurations.all {
    resolutionStrategy {
        force(
            "androidx.core:core:1.15.0",
            "androidx.core:core-ktx:1.15.0",
            "androidx.activity:activity:1.10.1",
            "androidx.activity:activity-ktx:1.10.1",
            "androidx.activity:activity-compose:1.10.1",
            "androidx.lifecycle:lifecycle-runtime:2.8.7",
            "androidx.lifecycle:lifecycle-runtime-ktx:2.8.7",
            "androidx.lifecycle:lifecycle-runtime-compose:2.8.7"
        )
    }
}
```

## Built with

- Kotlin
- Jetpack Compose and Material 3
- Firebase Authentication

## License

Academic project. Add the license of your choice here.
