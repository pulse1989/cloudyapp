package za.co.kernelpanic.cloudy.repository.db;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * no-op - There is no database to work on yet.
 */

public class DateConverter {

    @TypeConverter
    public static Date toDate(Long timestamp){
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimeStamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
