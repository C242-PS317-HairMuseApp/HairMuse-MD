# Hair Muse - Mobile Application

Hair Muse is a mobile application designed to assist users in identifying their face shape and providing tailored hairstyle recommendations. The app also offers additional features such as finding nearby salons and exploring articles about hair care.

---

## Features

### 1. **Face Shape Detection**
   - Detects one of five face shapes: **Oval, Round, Square, Oblong, Heart**.
   - Provides:
     - Tips specific to each face shape.
     - Detailed hairstyle recommendations for men and women.

### 2. **Nearest Salon**
   - Displays a list of salons closest to the user's location.
   - Integrated with location-based services to provide accurate salon suggestions.

### 3. **Articles**
   - Browse a curated list of articles about:
     - Hair care tips.
     - Styling tutorials.
     - Maintaining healthy hair.

---

## Tech Stack
- **Frontend**: Kotlin (or your preferred framework for the UI, if applicable).
- **Machine Learning**: Model trained using ResNet50, integrated with TensorFlow Lite for face shape detection.
- **API Deployment**: Dockerized API served on Google Cloud Run.

---

## Installation

1. Clone this repository:
   ```bash
   git clone https://github.com/your-repo/hair-muse.git
   ```
2. Navigate to the project directory:
   ```bash
   cd hair-muse
   ```
3. Install dependencies:
   ```bash
   flutter pub get
   ```
4. Run the application:
   ```bash
   flutter run
   ```

---

## How It Works

1. **Face Shape Detection**:
   - Upload a photo or use the camera to capture your face.
   - The app analyzes the image to determine your face shape.
   - Get personalized hairstyle recommendations instantly.

2. **Nearest Salon**:
   - Allow location access.
   - View a list of salons near your location.

3. **Articles**:
   - Explore various hair care and styling articles to enhance your hair routine.

## Screenshots
<div style="display: flex; justify-content: space-around;">
   <img src="https://github.com/HairMuseApp/.github/blob/main/assets/splash%20screen.png" alt="Splash Screen" width="210" height="450">
   <img src="https://github.com/HairMuseApp/.github/blob/main/assets/Home%20Screen.png" alt="Home Screen" width="185" height="450">
   <img src="https://github.com/HairMuseApp/.github/blob/main/assets/UploadButton.png" alt="Upload Button" width="185" height="450">
   <img src="https://github.com/HairMuseApp/.github/blob/main/assets/Prediction%20Screen.png" alt="Prediction Screen" width="190" height="450">
   <img src="https://github.com/HairMuseApp/.github/blob/main/assets/OutputScreen.png" alt="Output Screen" width="185" height="450">
</div>
