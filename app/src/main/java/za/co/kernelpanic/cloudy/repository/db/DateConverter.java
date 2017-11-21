package za.co.kernelpanic.cloudy.repository.db;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Thanks developer.android.com!
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
