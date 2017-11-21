package za.co.kernelpanic.cloudy.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Our entity information here works as follows
 * tableName - just the name of our sqlite table,
 * date index - we need unique values in our table, therefor we tell the databse this
 */
@Entity (tableName = "weather", indices = {@Index(value = {"date"}, unique = true)})
public class Forecast {

    @PrimaryKey (autoGenerate = true)
    private int id;
    @SerializedName("dt")
    private Date date;
    @SerializedName("day")
    private double dayForecast;
    @SerializedName("min")
    private double minTemp;
    @SerializedName("max")
    private double maxTemp;
    @SerializedName("double")
    private double pressure;
    @SerializedName("humidity")
    private double humidity;

    public Date getDate() {
        return date;
    }

    public double getDayForecast() {
        return dayForecast;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    //The constructor below is used by room
    public Forecast(int id, Date date, double dayForecast, double minTemp, double maxTemp, double pressure, double humidity) {
        this.id = id;
        this.date = date;
        this.dayForecast = dayForecast;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.pressure = pressure;
        this.humidity = humidity;
    }

}
