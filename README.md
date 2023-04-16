# WeatherApp
App to look up weather for a city using [Open Weather](https://openweathermap.org/api) APIs

Mostly for TDD and Compose practice
* `app` module has the view stuff
* `data` module handles the API calls, data manipulation, and passes states to the view.

This app requires an API key from OpenWeather.
-- Create a free account [here](https://home.openweathermap.org/users/sign_up) , generate a new key, and replace `"some_key"` in the `data` module's `build.gradle` file with your API key and build the app.

## In use
* Kotlin
* Jetpack Compose/Material3
* ViewModel
* Navigation
* Retrofit/Moshi
* LiveData, Coroutines, a bit of StateFlow
* DataStore Preferences
* Dagger Hilt
* JUnit4, JUnit5, Mockk, Mockito

## Future
* Room
* Implement device location
* TBD