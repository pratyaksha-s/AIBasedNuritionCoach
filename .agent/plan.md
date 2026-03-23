# Project Plan

TrackMyDiet is an Android app designed to help users track their diet and nutrition. 
Core features:
1. AI-powered calorie estimation: Users can take a photo of their food or describe it in a sentence to get estimated calories and macros.
2. Room Database: Local storage for food intake, goals, and user profile.
3. HomeScreen: Displays daily calorie progress with a floating action button (FAB) to add food.
4. Food History: A calendar view to review calorie intake for any selected day.
5. Profile Page: Displays user information, a weight tracking graph, and daily steps.
6. Onboarding/Goal Setting: A first-time flow to collect user data (age, gender, height, weight, activity level, goals) and use AI to suggest daily calorie and macro targets.
7. UI/UX: Material Design 3, vibrant and energetic color scheme, edge-to-edge display, and adaptive icon.

## Project Brief

# Project Brief: TrackMyDiet

TrackMyDiet is a modern Android application designed to simplify nutrition tracking through AI-powered insights and a seamless Material 3 user experience.

## Features
* **AI-Powered Nutrition Logging:** Users can capture a photo of their meal or provide a text description to receive instant estimates for calories and macronutrients.
* **Dynamic Dashboard:** A vibrant HomeScreen that visualizes daily calorie progress against targets, featuring a Floating Action Button (FAB) for quick food entry.
* **Personalized Onboarding:** A smart setup flow that collects user metrics (age, weight, activity level) and uses AI to calculate optimal daily nutritional goals.
* **Comprehensive Food History:** A dedicated calendar view allowing users to track their consistency and review nutritional intake for any previous date.
* **User Profile & Progress:** A central hub for managing user information and visualizing weight trends over time.

## High-Level Technical Stack
* **Language:** Kotlin
* **UI Framework:** Jetpack Compose with Material Design 3 (M3)
* **Concurrency:** Kotlin Coroutines & Flow
* **Architecture:** MVVM (Model-View-ViewModel) with Navigation Compose
* **Database:** Room Database for local persistence of meals and user data
* **Code Generation:** KSP (Kotlin Symbol Processing) for Room and Moshi
* **Networking:** Retrofit & OkHttp for AI API integrations
* **Image Loading:** Coil for rendering food captures and UI elements.

## Implementation Steps

### Task_1_Core_Data_and_Onboarding: Initialize the core infrastructure including the Room database for user and meal data, Retrofit for AI-powered nutrition analysis, and the Material 3 theme with a vibrant color scheme and edge-to-edge support. Implement the onboarding flow to collect user metrics and calculate nutritional goals.
- **Status:** COMPLETED
- **Updates:** Initialized core infrastructure (Room, Retrofit, Material 3 Theme) and implemented the onboarding flow. Added API key support via BuildConfig (GEMINI_API_KEY in local.properties). Verified build success.
- **Acceptance Criteria:**
  - Room database with User and Meal entities is configured
  - Retrofit service for AI analysis is initialized with API_KEY support
  - Material 3 theme with vibrant colors and edge-to-edge is applied
  - Onboarding flow successfully saves user goals to the database

### Task_2_Dashboard_and_AI_Logging: Develop the main dashboard displaying daily calorie and macro progress. Implement the AI food logging feature, allowing users to capture food photos via CameraX or provide text descriptions to get nutritional estimates.
- **Status:** COMPLETED
- **Updates:** Implemented the main dashboard displaying daily calorie and macro progress. Integrated AI food logging with CameraX for photo capture and Gemini 1.5 Flash for multimodal analysis of food images and text descriptions. Logged meals are persisted in Room and updated on the dashboard in real-time. Verified build success.
- **Acceptance Criteria:**
  - HomeScreen visualizes daily calorie progress against targets
  - CameraX integration captures food photos for analysis
  - AI service correctly parses photo/text into calories and macros
  - Logged meals are persisted and updated on the dashboard in real-time

### Task_3_History_Profile_and_Assets: Implement the food history calendar view and the user profile page with weight tracking charts. Create an adaptive app icon and refine the UI for Material 3 compliance.
- **Status:** COMPLETED
- **Updates:** Implemented the food history calendar view and the user profile page with weight tracking charts. Created an adaptive app icon and refined the UI for Material 3 compliance.
Acceptance criteria: [History screen allows reviewing nutritional intake by selecting dates in a calendar, Profile page displays user info and a weight progress graph, Adaptive app icon is generated and applied to the project, UI follows vibrant Material 3 design guidelines]
- **Acceptance Criteria:**
  - History screen allows reviewing nutritional intake by selecting dates in a calendar
  - Profile page displays user info and a weight progress graph
  - Adaptive app icon is generated and applied to the project
  - UI follows vibrant Material 3 design guidelines
- **Duration:** N/A

### Task_4_Final_Verification: Perform a comprehensive run and verify of the application to ensure stability, requirement alignment, and high-quality UX.
- **Status:** IN_PROGRESS
- **Acceptance Criteria:**
  - The app builds and runs without crashes
  - All existing tests pass
  - AI logging, onboarding, and history features are fully functional
  - Verify application stability and alignment with project brief
- **StartTime:** 2026-03-22 15:46:48 IST

