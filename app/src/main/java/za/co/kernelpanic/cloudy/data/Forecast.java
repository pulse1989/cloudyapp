package za.co.kernelpanic.cloudy.data;


import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Forecast {

    @SerializedName("temp")
    private Temperature temperature;
    @SerializedName("weather")
    private List<Weather> weather;
    @SerializedName("pressure")
    private double pressure;
    @SerializedName("humidity")
    private double humidity;

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public List<Weather> getWeather() {
        return weather;
    }




}
