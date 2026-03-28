# AI nutrition coach 🍎

AI nutrition coach is a modern Android application designed to simplify nutrition tracking using AI-powered insights. Built with Jetpack Compose and Material 3, it provides a seamless experience for users to log meals via photos or text, track daily progress, and monitor weight trends.

## Features ✨

*   **AI-Powered Food Logging:** Capture a photo of your meal or provide a text description. The app uses **Gemini 1.5 Flash** to estimate calories and macronutrients (protein, carbs, fats) instantly.
*   **Smart Onboarding:** personalized goal setting based on your age, gender, height, weight, and activity level. AI suggests your optimal daily nutritional targets.
*   **Interactive Dashboard:** Visualize your daily calorie intake against your targets with vibrant Material 3 progress rings.
*   **Comprehensive History:** Review your past meals and nutritional consistency using a calendar view. The calendar collapses into a week view for better focus while scrolling.
*   **Progress Tracking:** Manage your profile and track your weight journey with an integrated progress chart.

## Tech Stack 🛠️

*   **UI:** Jetpack Compose with Material 3 (M3).
*   **Architecture:** MVVM (Model-View-ViewModel).
*   **Database:** Room for robust local data persistence.
*   **Networking:** Retrofit & OkHttp for API communication.
*   **AI:** Google Gemini API (multimodal analysis).
*   **Camera:** CameraX for seamless food capture.
*   **Image Loading:** Coil.
*   **Concurrency:** Kotlin Coroutines & Flow.

## Setup & Installation 🚀

### Prerequisites
- Android Studio Ladybug or newer.
- A Gemini API Key from [Google AI Studio](https://aistudio.google.com/).

### Steps to Clone and Run

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/yourusername/AI-nutrition-coach.git
    ```

2.  **Open the project:**
    Open Android Studio and select **File > Open**, then navigate to the cloned directory.

3.  **Add your API Key:**
    Open the `local.properties` file in the project root and add your Gemini API key:
    ```properties
    GEMINI_API_KEY=your_actual_api_key_here
    ```

4.  **Sync Gradle:**
    Click on "Sync Project with Gradle Files" in the top toolbar.

5.  **Run the app:**
    Select an emulator or a physical device and click the **Run** button.

## Project Structure 📁

- `data/`: Contains Room entities, DAOs, and the Repository layer.
- `ui/`: Compose screens, ViewModels, and theme configurations.
- `remote/`: Retrofit API service and Gemini request/response models.

---
*Note: This app requires an active internet connection for food analysis and goal calculation features.*
