package za.co.kernelpanic.cloudy.data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;



public class Forecast {

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

}
