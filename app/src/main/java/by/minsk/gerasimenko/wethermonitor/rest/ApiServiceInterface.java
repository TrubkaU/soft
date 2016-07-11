package by.minsk.gerasimenko.wethermonitor.rest;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created 11.07.2016.
 */
public interface ApiServiceInterface {

    @GET("weather")
    Call<JsonObject> getWeatherBy(@Query("lat") double lat,
                                  @Query("lon") double lng,
                                  @Query("appid") String key);

}
