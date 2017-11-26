### Cloudy - A Simple Android Weather App

The "Cloudy" weather app architecture image is below:

![app arch](https://image.ibb.co/kpCs2m/cloudy_2.png)

As we see, the repository is the single source of truth for the entire app, handling the interaction between the gps service in order to get updates and the api service to get the data, which will be sent back to the UI. 

The app makes use of the Android Achitecture components in order to get the data:

* **ViewModel** -  in order to prepare data for the UI while respecting the lifecycle of the app

* **LiveData** - In order to store data from remote source and present it to the app via the ViewModel. LiveData is also lifecycle aware. 

In order to start the data retrieval process, there must be at least one observer. In this case it will be our WeatherFragment. 

#### The Order of operation is as follows:

* The Activity does some permission checks before loading the fragment.

* Once  the Fragment is loaded, connectivity checks and GPS settings checks are performed. If either setting is off, the user is prompted to fix the issue. 

* Once the settings are fixed, the  fragment can begin trying to retrive the weather via the architecture in the diagram above.

When the app is in a suspended state, we make sure the  LocationProvider stops asking for location updates, respecting the user's battery life. 

#### Cloudy makes use of the following libraries:

* Android Architecture components (as mentioned above, LiveData and ViewModel) ver 1.0 stable
* Dagger 2.11 (incl. dagger.android)
* Firebase for crashlytics reporting.
* Retrofit for managing our api calls.
* Okhttp3 for intercepting all our API calls to make sure the request and response are accurate

