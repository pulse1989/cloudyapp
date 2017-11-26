### Cloudy - A Simple Android Weather App

The "Cloudy" weather app architecture image is below:

![app arch](https://image.ibb.co/kpCs2m/cloudy_2.png)

As we see, the repository is the single source of truth for the entire app, handling the interaction between the gps service in order to get updates, the api service to get the data, which will be sent back to the UI. 

The app makes use of the new Android Achitecture components in order to get the data:

* The ViewModel -  in order to prepare data for the UI while respecting the lifecycle of the app

* The LiveData - In order to store data from remote source and present it to the app via the ViewModel. LiveData is also lifecycle aware. 

In order to start the data retrieval process, there must be at least one observer. In this case it will be our WeatherFragment. 



