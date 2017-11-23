package za.co.kernelpanic.cloudy.repository.db;


//import android.arch.persistence.room.Database;
//import android.arch.persistence.room.Room;
//import android.arch.persistence.room.RoomDatabase;
//import android.arch.persistence.room.TypeConverters;
//import android.content.Context;
//
//import za.co.kernelpanic.cloudy.data.Forecast;
//
///**
// * Our database will be our caching mechanism for the app. We first need to check to make sure we have the weather
// * data here before going off to the internet and checking for data. Once we get data, it will be stored here
// *
// * Would have loved to implement this for 7 day forecast. but time is of the essence...
// */
//
//@Database(entities = {Forecast.class}, version = 1)
//@TypeConverters(DateConverter.class)
//public abstract class CloudyDatabase extends RoomDatabase {
//
//    private static CloudyDatabase INSTANCE;
//
//    public abstract ForecastDao forecastDao();
//
//
//    public static CloudyDatabase getInstance(Context context) {
//
//        if(INSTANCE == null) {
//
//             because we don't want other threads to be up to no good while we perform this action
//            synchronized (CloudyDatabase.class) {
//
//                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CloudyDatabase.class, "weather.db").build();
//            }
//        }
//
//        return INSTANCE;
//    }
//
//}
