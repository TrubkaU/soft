package by.minsk.gerasimenko.wethermonitor.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created 11.07.2016.
 */
public class WeatherResponse  {

    @SerializedName("dt")
    long time;
    Clouds clouds;
    Wind wind;
    Main main;

    public long getTime() {
        return time;
    }

    public int getClouds() {
        return (clouds != null) ? clouds.all : -1;
    }

    public Double getTemperature() {
        return main != null ? main.temperature:null;
    }

    public String getWindSpeed() {
        return  wind!=null ?  wind.speed:"";
    }

    static class Main {

        @SerializedName("temp")
        Double temperature;
        int pressure;
        int humidity;
        @SerializedName("temp_min")
        Double minTemperature;
        @SerializedName("temp_max")
        Double maxTemperature;
     }

    static class Wind {

        String speed;
        int deg;
    }

    static class Clouds {
        int all;
    }
}
