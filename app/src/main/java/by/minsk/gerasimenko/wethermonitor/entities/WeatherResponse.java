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
        return (clouds != null && clouds.all != null) ? clouds.all.all : -1;
    }

    public String getTemperature() {
        return main != null ? main.temperature:"";
    }

    public String getWindSpeed() {
        return  wind!=null ?  wind.speed:"";
    }


    static class Main {

        @SerializedName("temp")
        String temperature;
        int pressure;
        int humidity;
        @SerializedName("temp_min")
        String minTemperature;
        @SerializedName("temp_max")
        String maxTemperature;
     }

    static class Wind {

        String speed;
        int deg;
    }

    static class Clouds {
        All all;
        static class All{
            int all;
        }
    }
}


/*
    {"coord":
        {"lon":145.77,"lat":-16.92},
        "weather":[{"id":803,"main":"Clouds","description":"broken clouds","icon":"04n"}],
        "base":"cmc stations",
            "main":{"temp":293.25,"pressure":1019,"humidity":83,"temp_min":289.82,"temp_max":295.37},
        "wind":{"speed":5.1,"deg":150},
        "clouds":{"all":75},
        "rain":{"3h":3},
        "dt":1435658272,
            "sys":{"type":1,"id":8166,"message":0.0166,"country":"AU","sunrise":1435610796,"sunset":1435650870},
        "id":2172797,
            "name":"Cairns",
            "cod":200}
*/
