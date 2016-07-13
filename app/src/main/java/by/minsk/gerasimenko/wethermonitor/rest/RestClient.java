package by.minsk.gerasimenko.wethermonitor.rest;

import by.minsk.gerasimenko.wethermonitor.Const;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created 11.07.2016.
 */
public class RestClient {

    public static volatile RestClient instance;

    ApiServiceInterface apiServiceInterface;


    public static RestClient getInstance() {
        RestClient localInstance = instance;
        if (localInstance == null) {
            synchronized (RestClient.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new RestClient();
                }
            }
        }
        return localInstance;
    }

    public RestClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Const.WEATHER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        apiServiceInterface = retrofit.create(ApiServiceInterface.class);
    }

    public ApiServiceInterface getApi() {
        return apiServiceInterface;
    }

}
