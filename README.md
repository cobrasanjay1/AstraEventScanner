# Astra Event Scanner 📱

**Astra Event Scanner** is a modern Android application designed for fast and reliable event ticket verification using QR codes.

The application allows authorized event staff to scan attendee QR codes, validate ticket information through a backend API, and manage event check-ins directly from an Android device.

Built with **Kotlin**, **Jetpack Compose**, **CameraX**, and **Google ML Kit**, the app provides a modern, responsive, and easy-to-use scanning experience.

---

## ✨ Features

* 📷 **Fast QR Code Scanning**

  * Uses Android CameraX for camera handling.
  * Uses Google ML Kit Barcode Scanning for QR code detection.

* 🎟️ **Event Ticket Verification**

  * Scanned QR data can be sent to the configured backend API for validation.
  * Helps prevent invalid or duplicate ticket check-ins.

* 🔐 **Google Authentication**

  * Supports Google sign-in using Android Credential Manager and Google Identity Services.
  * Designed for authenticated event staff and organizers.

* 🌐 **Backend API Integration**

  * REST API communication using Retrofit.
  * OkHttp handles network communication and logging.

* 🧭 **Jetpack Compose Navigation**

  * Screen navigation is handled using Navigation Compose.
  * Provides a clean and scalable application structure.

* 💾 **Local Preferences**

  * Uses Android DataStore for storing application preferences and local state.

* 🎨 **Material 3 UI**

  * Built using Jetpack Compose and Material 3.
  * Responsive modern Android interface.

* 🚀 **Splash Screen**

  * Uses the AndroidX SplashScreen API for a smooth application startup experience.

---

## 🛠️ Tech Stack

| Technology                     | Purpose                            |
| ------------------------------ | ---------------------------------- |
| **Kotlin**                     | Primary programming language       |
| **Jetpack Compose**            | Declarative UI                     |
| **Material 3**                 | UI components and design system    |
| **CameraX**                    | Camera access and preview          |
| **Google ML Kit**              | QR / barcode scanning              |
| **Retrofit**                   | REST API client                    |
| **OkHttp**                     | HTTP networking                    |
| **Gson**                       | JSON serialization/deserialization |
| **DataStore**                  | Local preference storage           |
| **Navigation Compose**         | Application navigation             |
| **Android Credential Manager** | Authentication                     |
| **Google Identity Services**   | Google Sign-In                     |
| **AndroidX Lifecycle**         | Lifecycle and ViewModel support    |
| **AndroidX SplashScreen**      | Application splash screen          |
| **Gradle Version Catalog**     | Dependency management              |

The current application module explicitly includes CameraX, ML Kit barcode scanning, Retrofit, OkHttp, DataStore, Navigation Compose, Credential Manager, and Google authentication dependencies.

---

## 🏗️ Architecture

The application follows a modular Android architecture with clear separation between UI, navigation, business logic, and networking.

```text
┌─────────────────────────────────────┐
│          Jetpack Compose UI         │
│                                     │
│  Screens / Components / Material 3 │
└──────────────────┬──────────────────┘
                   │
                   ▼
┌─────────────────────────────────────┐
│             Navigation              │
│                                     │
│        Navigation Compose           │
└──────────────────┬──────────────────┘
                   │
                   ▼
┌─────────────────────────────────────┐
│          Application Logic          │
│                                     │
│       ViewModels / State            │
└───────────────┬─────────┬───────────┘
                │         │
        ┌───────▼───┐ ┌──▼──────────┐
        │ QR Scanner │ │ DataStore   │
        │            │ │ Preferences │
        │ CameraX    │ └─────────────┘
        │ ML Kit     │
        └──────┬─────┘
               │
               ▼
┌─────────────────────────────────────┐
│             API Layer               │
│                                     │
│       Retrofit + OkHttp + Gson      │
└──────────────────┬──────────────────┘
                   │
                   ▼
┌─────────────────────────────────────┐
│             Backend API             │
│                                     │
│       Authentication / Events       │
│       Ticket Validation / Check-in  │
└─────────────────────────────────────┘
```

---

## 📱 Application Flow

A typical ticket scanning flow looks like this:

```text
          Start Application
                 │
                 ▼
            Login / Auth
                 │
                 ▼
          Scanner Screen
                 │
                 ▼
          Open CameraX
                 │
                 ▼
          Detect QR Code
                 │
                 ▼
        Extract QR Payload
                 │
                 ▼
       Send Data to Backend
                 │
                 ▼
        Validate Ticket
           /          \
          /            \
       Valid          Invalid
         │               │
         ▼               ▼
    Check In         Show Error
         │
         ▼
   Display Result
```

---

## 📂 Project Structure

The project is organized as a standard Android application:

```text
AstraEventScanner/
│
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/
│   │       │       └── astra/
│   │       │           └── eventscanner/
│   │       │
│   │       ├── res/
│   │       │
│   │       └── AndroidManifest.xml
│   │
│   └── build.gradle.kts
│
├── gradle/
│   └── libs.versions.toml
│
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
└── README.md
```

The Android application namespace and application ID are `com.astra.eventscanner`.

---

## ⚙️ Requirements

Before building the project, make sure you have:

* Android Studio
* Android SDK
* JDK 11 or compatible configured Java environment
* Android device or emulator
* Camera access for QR scanning
* Internet connection for backend communication

### Android Compatibility

| Setting            | Value |
| ------------------ | ----: |
| Minimum SDK        |    26 |
| Target SDK         |    37 |
| Compile SDK        |    37 |
| Java compatibility |    11 |
| Version            |   1.0 |

The application requires Android camera and internet permissions.

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/cobrasanjay1/AstraEventScanner.git
```

Navigate into the project:

```bash
cd AstraEventScanner
```

---

### 2. Open the project

Open the project in **Android Studio**.

Allow Gradle to synchronize and download the required dependencies.

---

### 3. Configure local properties

The application reads environment-specific configuration from `local.properties`.

Create or update:

```text
local.properties
```

Add the required configuration:

```properties
sdk.dir=/path/to/Android/sdk

API_BASE_URL=https://your-api-domain.com/

GOOGLE_CLIENT_ID=your-google-client-id
```

> **Important:** Do not commit `local.properties` or private credentials to Git.

The application uses `API_BASE_URL` and `GOOGLE_CLIENT_ID` to generate build-time configuration values.

---

### 4. Build the application

Using the Gradle wrapper:

```bash
./gradlew build
```

On Windows:

```powershell
gradlew.bat build
```

---

### 5. Run the application

Connect an Android device with USB debugging enabled or start an Android emulator.

Then run:

```bash
./gradlew installDebug
```

Or simply press **Run ▶** in Android Studio.

---

## 🔑 Google Sign-In Configuration

The application uses Android Credential Manager and Google Identity Services for Google authentication.

You need to configure a Google OAuth client for the application.

The Android package/application ID is:

```text
com.astra.eventscanner
```

For local development, provide the client ID through:

```properties
GOOGLE_CLIENT_ID=your-google-client-id
```

The corresponding value is injected into the Android `BuildConfig` during the Gradle build.

For production deployments, make sure the appropriate SHA-1/SHA-256 signing certificate fingerprints are configured in Google Cloud / Firebase as required by the authentication setup.

---

## 📷 QR Scanner

The scanner uses:

```text
CameraX
   +
Google ML Kit Barcode Scanning
```

CameraX manages the camera lifecycle and preview, while ML Kit processes camera frames to detect QR/barcode data.

The application requests the following camera-related permissions/features:

```xml
<uses-permission android:name="android.permission.CAMERA" />

<uses-feature
    android:name="android.hardware.camera"
    />

<uses-feature
    android:name="android.hardware.camera.autofocus"
    />
```

The app also requires Internet access for API communication.

---

## 🌐 API Configuration

The backend URL is configured through:

```properties
API_BASE_URL=https://your-api-domain.com/
```

The value is exposed to the application through:

```kotlin
BuildConfig.API_BASE_URL
```

This allows the same Android codebase to be used with different backend environments.

For example:

```text
Development
    ↓
https://dev-api.example.com/

Testing
    ↓
https://test-api.example.com/

Production
    ↓
https://api.example.com/
```

---

## 🔒 Security Considerations

Do not commit sensitive configuration to the repository.

### Never commit

```text
local.properties
API keys
OAuth secrets
Private signing keys
Keystores
Backend credentials
Access tokens
```

Use local configuration for development and secure CI/CD secrets for automated builds.

The Android manifest currently enables cleartext traffic:

```xml
android:usesCleartextTraffic="true"
```

For production deployments, this should be reviewed and restricted if the backend exclusively uses HTTPS.

---

## 🧪 Testing

Run unit tests with:

```bash
./gradlew test
```

Run Android instrumentation tests with:

```bash
./gradlew connectedAndroidTest
```

You can also run tests directly through Android Studio.

---

## 🐛 Troubleshooting

### Camera does not open

Check that:

1. Camera permission has been granted.
2. The device has a working camera.
3. Camera access is not blocked by another application.
4. The application is running on Android API 26 or newer.

---

### Google Sign-In fails

Check:

```text
GOOGLE_CLIENT_ID
```

Then verify:

* Correct package name
* Correct SHA-1/SHA-256 certificate
* Correct OAuth client type
* Google authentication configuration
* Debug vs release signing configuration

---

### API requests fail

Check:

```properties
API_BASE_URL=https://your-api-domain.com/
```

Then verify:

* Device has Internet access.
* Backend is running.
* URL is reachable from the device.
* HTTPS certificate is valid.
* API endpoint paths match the Android client.

---

### Gradle build fails

Try:

```bash
./gradlew clean
```

Then:

```bash
./gradlew build
```

If Gradle dependencies are corrupted:

```bash
./gradlew --refresh-dependencies build
```

---

## 🛣️ Roadmap

Potential future improvements include:

* [ ] Offline ticket verification
* [ ] Duplicate scan protection
* [ ] Scan history
* [ ] Event-specific scanner sessions
* [ ] Real-time check-in statistics
* [ ] Organizer dashboard
* [ ] Improved error handling
* [ ] Automatic retry for failed API requests
* [ ] Offline scan queue
* [ ] Improved accessibility
* [ ] Automated UI tests
* [ ] Production release configuration
* [ ] App performance monitoring

---

## 🤝 Contributing

Contributions are welcome.

1. Fork the repository.
2. Create a feature branch:

```bash
git checkout -b feature/my-feature
```

3. Make your changes.
4. Commit your changes:

```bash
git add .
git commit -m "Add my feature"
```

5. Push the branch:

```bash
git push origin feature/my-feature
```

6. Open a Pull Request.

---

## 📄 License

Add your preferred open-source license to the repository.

For example:

```text
MIT License
```

If this project is not intended to be open source, remove this section and specify the appropriate usage terms.

---

## 👨‍💻 Author

**Cobra Sanjay**

GitHub:

https://github.com/cobrasanjay1

Project:

https://github.com/cobrasanjay1/AstraEventScanner

---

## ⭐ Support

If you find this project useful, consider giving the repository a ⭐ on GitHub.

---

## 📸 Screenshots

Add screenshots of the application here:

```text
docs/
├── login.png
├── scanner.png
├── scan-result.png
└── event-screen.png
```

Example:

```markdown
## Screenshots

| Login | Scanner | Result |
|---|---|---|
| ![Login](docs/login.png) | ![Scanner](docs/scanner.png) | ![Result](docs/scan-result.png) |
```

---

## 📌 Project Status

**Status:** Active Development

Astra Event Scanner is currently under development and may receive changes to its UI, backend integration, authentication flow, and ticket validation logic.
