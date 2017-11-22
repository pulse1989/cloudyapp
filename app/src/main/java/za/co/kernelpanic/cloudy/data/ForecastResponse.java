package za.co.kernelpanic.cloudy.data;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastResponse {

    @SerializedName("list")
    private List<Forecast> forecastList;
    private City city;


    public City getCity() {
        return city;
    }

    public List<Forecast> getForecastList() {
        return forecastList;
    }

}
