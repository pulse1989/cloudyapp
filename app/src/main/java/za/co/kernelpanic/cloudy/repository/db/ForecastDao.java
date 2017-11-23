package za.co.kernelpanic.cloudy.repository.db;


//import android.arch.lifecycle.LiveData;
//import android.arch.persistence.room.Dao;
//import android.arch.persistence.room.Insert;
//import android.arch.persistence.room.OnConflictStrategy;
//import android.arch.persistence.room.Query;
//
//import java.util.Date;
//import java.util.List;
//
//import za.co.kernelpanic.cloudy.data.Forecast;
//
///**
// * We use this DAO to access information from our database via normal SQL database queries
// * Room will generate an implementation of this class at runtime and use that in order to run our database queries
// * returning the result with the given type below.
// */
//
//@Dao
//
//public interface ForecastDao {
//
//    /*
//     * lets dump all our weather forecasts into the database
//     * The number of days can be adjusted.
//     */
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void bulkInsert(Forecast...forecasts);
//
//    here we can grab all forecasts that are from today onwards
//    @Query("SELECT * from weather  WHERE date >= :date")
//    LiveData<Forecast> getWeatherForecastFromToday(Date date);
//
//    we dont need weather older than today, so we delete it
//
//    @Query("DELETE FROM weather where date < :date")
//    void deleteStaleWeather(Date date);
//
//}
