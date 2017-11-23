package za.co.kernelpanic.cloudy.data;


import com.google.gson.annotations.SerializedName;

public class Weather {

    private String main;
    private String description;

    @SerializedName("id")
    private int iconId;

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    public int getIconId() {
        return iconId;
    }


}
