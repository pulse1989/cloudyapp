package za.co.kernelpanic.cloudy.repository.db

import java.util.Date

/**
 * no-op - There is no database to work on yet.
 */

object DateConverter {

    //    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    //    @TypeConverter
    fun toTimeStamp(date: Date?): Long? {
        return date?.time
    }
}
