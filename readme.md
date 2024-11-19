# UxCam SDK for Android

The UxCam SDK helps track user behavior, session data, and events in your Android application. It
provides functionality to capture user sessions, record events during a session, and sync session
data with a remote server.
  
---  

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
- [Architecture](#architecture)
    - [Core Components](#core-components)
    - [Database Design](#database-design)
    - [Sync Process](#sync-process)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

---  

## Overview

The UxCam SDK is a tool for Android developers to track user behavior in their apps. The SDK
captures events like button clicks, screen transitions, and user sessions. This data is then stored
locally and can be synced with a remote server (e.g., Firestore) for analysis and
insights.
  
---  

## Features

- **Session Management**: Start and end sessions programmatically.
- **Event Tracking**: Track custom events (e.g., button clicks, page views).
- **Data Persistence**: Store session and event data locally using Room Database.
- **Background Sync**: Sync session and event data to a remote server using WorkManager.
- **Device Info**: Collect device-specific data (e.g., device ID).
- **Logger**: Built-in logging for debugging.

---  

## Getting Started

### Prerequisites

Before you can integrate the UxCam SDK into your app, you need:

- Android Studio (latest version)
- An Android project
- Firebase account (To upload data to firestore)

### Installation

1. **Add dependencies to your `build.gradle.kts`**:

   In your `libs.versions.toml`, add:

  ```
     androidx_core = "1.9.0" 
     androidx_lifecycle = "2.5.1" 
     firebase_firestore = "24.2.1" 
     hilt = "2.44"  
```

2. **Add dependencies to your `build.gradle.kts`**:

   ```
   dependencies {  
       implementation(libs.androidx.core.ktx)  
       implementation(libs.androidx.appcompat)  
       implementation(libs.firebase.firestore)  
       implementation(libs.hilt.android)  
       ksp(libs.hilt.android.compiler)  
       implementation(project(":uxcamSdk"))  
   }
   ```  

3. **Initialize UxCam SDK**:

   In your application class (`Application.kt`) extend  (`UxCamApplication.kt`)  and initialize
   Firestore before `onCreate()`.

   ```
   class MyApplication : UxCamApplication() {  
       override lateinit var firestore = FirebaseFirestore
       override fun onCreate() {  
           FirebaseApp.initialize(this)
           firestore = FirebaseFirestore.getInstance()
           super.onCreate()  
       }  
   }
   ```  

  
----------  

## Architecture

### Core Components

1. **UxCam.kt**:

    - Initializes and configures the SDK.
    - Provides methods for session management and event tracking.
2. **SessionManager.kt**:

    - Manages the user session lifecycle (start, end).
    - Tracks events during the session and stores them locally.
3. **SyncWorker.kt**:

    - Responsible for syncing session data with a remote server in the background.
4. **SyncHelper.kt**:

    - A helper class for preparing and handling data before uploading it to the server.

### Database Design

- **UxSession**:

    - Represents a user session.
    - Contains `id`, `startTime`, `endTime`, and user-related identifiers (e.g., email, device ID,
      package name).
- **UxEvent**:

    - Represents an event that happens within a session (e.g., button clicks, page views).
    - Contains `sessionId`, `name`, and `properties`.

### Sync Process

1. **Session Data** is stored locally in a Room database as a `UxSession` entity.
2. **Events** that occur during the session are also stored locally as `UxEvent` entities.
3. **SyncWorker** uploads the session and event data to a remote server (e.g., Firestore).
4. After successful syncing, the local records are cleared from the database.

## Usage

1. **Setup Identifiers**: To setup call the following method

   ```
   UxCam.setIdentifier("com.jhondoe.uxcam")
        .setName("Jhon Doe")
        .setEmail("abnormal.bbk@gmail.com)
        .build(this)
   ```  
2. **Start a Session**: To start a new session, call the following method after initializing the
   SDK:

   `UxCam.startSession()`

3. **Track an Event**: To track an event, use:

   `UxCam.addEvent("button_click", mapOf("button_name" to "start_button"))`

4. **End a Session**: To end the current session:

   `UxCam.endSession()`

5. **Sync Data**: The sync is done manually in next app start

## License

This project is licensed under the MIT License - see the LICENSE file for details.  
  
