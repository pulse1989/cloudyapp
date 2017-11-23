package za.co.kernelpanic.cloudy.data;

import com.google.gson.annotations.SerializedName;


public class Temperature {

    @SerializedName("dt")
    private long date;
    @SerializedName("day")
    private double dayForecast;
    @SerializedName("min")
    private double minTemp;
    @SerializedName("max")
    private double maxTemp;


    public long getDate() {
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


    //The constructor below is used by room
    public Temperature(long date, double dayForecast, double minTemp, double maxTemp) {

        this.date = date;
        this.dayForecast = dayForecast;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }
}
