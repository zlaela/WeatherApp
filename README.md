# WeatherApp
An Android weather application built with Jetpack Compose that provides current weather and 5-day forecasts for any city using the [OpenWeather API](https://openweathermap.org/api).

## Features
- Current Weather: Current weather conditions: temperature, feels-like, and weather descriptions
- 5-Day Forecast: Extended weather predictions with day/night temperature variations
- City Search: Search and select cities with autocomplete functionality
- Data Persistence: Remembers the last searched city using DataStore
- Pull to refresh: Updates the weather and forecast for the current location

## Architecture
- `app` module: UI layer with Compose screens, ViewModels, and navigation
- `data` module: Data layer handling API calls, local storage (in-progress), and business logic

## Tech Stack
### Core Technologies
- Kotlin
- Jetpack Compose
- Material3
- Navigation Compose

### Architecture & Dependency Injection
- MVVM Pattern
- Dagger Hilt
- ViewModel
- LiveData

### Networking & Data
- Retrofit
- Moshi
- OkHttp
- DataStore Preferences
- Room Database (implementation in-progress)

### Location Services (Unused - for future enhancement)
- Google Play Services Location
- FusedLocationProviderClient

### Testing
- JUnit5 - Unit testing framework
- MockK - Mocking library for Kotlin
- MockWebServer - HTTP server for testing network calls
- Compose Testing - UI testing for Compose components
- Espresso - UI testing framework

## Setup Instructions

### Prerequisites
- OpenWeather API key

### API Key Configuration
1. Create a free account at [OpenWeather](https://home.openweathermap.org/users/sign_up)
2. Generate an API key from your dashboard
3. Create a `local.properties` file in the project root:
```properties  
BASE_URL=https://api.openweathermap.org/data/2.5/
API_KEY=your_api_key_here
```  

### Build and Run
1. Clone the repository
2. Add your API key to `local.properties`
3. Sync the project with Gradle
4. Run the app on an emulator or device

## Permissions
The app requires the following permissions:
- `INTERNET` - For API calls
- `ACCESS_COARSE_LOCATION` - For approximate location (TODO)
- `ACCESS_FINE_LOCATION` - For precise location (TODO)

## Project Structure
```  
app/  
├── src/main/java/com/example/weatherapp/  
│   ├── ui/                    # Compose UI components  
│   │   ├── composable/        # Reusable UI components  
│   │   ├── screens/          # Screen-level composables  
│   │   └── navigation/       # Navigation setup  
│   ├── viewmodel/           # ViewModels for state management  
│   └── utils/               # Utility classes (LocationHelper, etc.)  
  
data/  
├── src/main/java/com/example/data/  
│   ├── api/                 # Retrofit API interfaces  
│   ├── domain/              # Domain models and mappers  
│   ├── repository/          # Repository implementations  
│   ├── store/               # DataStore and Room implementations  
│   └── di/                  # Dependency injection modules  
```  

## Future Enhancements
- Automatic weather search
- Enhanced weather visualizations
- Weather alerts and notifications
- Offline weather data caching
- Multiple location support
- Weather widgets
- Prettier UI
