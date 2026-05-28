# Biometric Attendance App (Kotlin)

Mobile attendance application developed in Kotlin for **CS 4405 - Mobile Applications** at University of the People.

<img width="259" height="574" alt="Attendance Record" src="https://github.com/user-attachments/assets/dcd98ed8-e541-4228-836e-6f870666da06" />

## Features

- User sign up with:
  - Name
  - Email validation
  - Password setup
- User sign in with email and password
- Store user data in local database
- Biometric authentication support
- GPS location verification for office check-in/check-out
- Attendance record management
- Prevent multiple check-ins/check-outs
- View attendance history

## Technologies Used

- Kotlin
- Android SDK
- SQLite Database
- Android Biometric API
- Google Play Services Location API

## Permissions

The following permissions are required:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.USE_BIOMETRIC" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
```

## Dependencies

```kotlin
implementation("androidx.biometric:biometric:1.2.0-alpha05")
implementation("com.google.android.gms:play-services-location:21.3.0")
```

## Project Structure

```text
SignupActivity.kt
SetPasswordActivity.kt
SigninActivity.kt
HomeActivity.kt
CheckinActivity.kt
CheckoutActivity.kt
AttendanceActivity.kt

/Database
    DatabaseHandler.kt

/model
    User.kt
    AttendanceObject.kt
```

## Biometric Authentication

Fingerprint data is not stored in the application database.  
Android securely stores biometric data on the device and does not allow direct access to fingerprint images or templates.

## Office Location

The application validates user location before check-in/check-out.

```text
Latitude: 37.4219983
Longitude: -122.084
```

## Images

<img width="653" height="476" alt="Screenshot 2026-05-28 at 20 03 54" src="https://github.com/user-attachments/assets/a9eae957-187f-4ce4-bf77-e650c68ea257" />

<img width="686" height="492" alt="Screenshot 2026-05-28 at 20 04 29" src="https://github.com/user-attachments/assets/44a0656b-1082-4fd3-b49c-f7442b3ab88e" />

<img width="471" height="501" alt="Screenshot 2026-05-28 at 20 04 49" src="https://github.com/user-attachments/assets/a4ab030f-d9a8-47c8-8559-6845981e7ac8" />

<img width="425" height="466" alt="Screenshot 2026-05-28 at 20 05 07" src="https://github.com/user-attachments/assets/b8bb3020-f00b-4b07-932d-4df066d96ccd" />

