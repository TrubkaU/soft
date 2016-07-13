package by.minsk.gerasimenko.wethermonitor.mvp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import by.minsk.gerasimenko.wethermonitor.Const;
import by.minsk.gerasimenko.wethermonitor.R;
import by.minsk.gerasimenko.wethermonitor.db.DBHelper;
import by.minsk.gerasimenko.wethermonitor.entities.Place;
import by.minsk.gerasimenko.wethermonitor.entities.Statistic;
import by.minsk.gerasimenko.wethermonitor.entities.TemperatureUnit;
import by.minsk.gerasimenko.wethermonitor.entities.WeatherResponse;
import by.minsk.gerasimenko.wethermonitor.rest.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created 11.07.2016.
 */
public class MainPresenter extends MvpBasePresenter<MainView> {

    private final static String CAPACITY = "cap";
    private final static String UNIT = "unit";

    private Location currLocation;
    private Place place;
    private List<Statistic> statisticList;

    private int capacity;
    private TemperatureUnit unit;

    public MainPresenter() {
        this.statisticList = new ArrayList<>();

    }

    public void onStart() {
        if (getView()!= null) {

            SharedPreferences preferences = getView().getPreferences();
            unit = preferences.getBoolean(UNIT,false)? TemperatureUnit.CELCII :TemperatureUnit.KELVIN;
            capacity = preferences.getInt(CAPACITY,2);
        }

       getFromDB();
    }

    public List<Statistic> getStatisticList() {
        return statisticList;
    }

    private void getWeatherByLocale() {
        if (isInternetConnected()) {
            if (currLocation != null) {
                RestClient.getInstance().getApi().getWeatherByLocale(
                        currLocation.getLatitude(),
                        currLocation.getLongitude(),
                        Const.API_KEY).enqueue(new WeatherCallBack());
            }
        } else {
            if (isViewAttached() && getView()!= null) {

                getView().showToast(getView().getContext().getString(R.string.no_iternet_fail));
                getView().closeProgressBar();
            }
        }
    }

    private void getWeatherByCity(String city) {
        RestClient.getInstance().getApi()
                .getWeatherByCity(city,Const.API_KEY)
                .enqueue(new WeatherCallBack());
    }

    public void setCurrLocation(Location location) {
        try {
            currLocation = location;
            if (getView()!=null) {
                List<Address> address = new Geocoder(getView().getContext(),new Locale(Const.LOCATION))
                        .getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                if (address!= null && !address.isEmpty()) {
                    String country  = address.get(0).getCountryName();
                    String city  = address.get(0).getLocality();

                    place = new Place(country,city);

                    getView().initActionBar(city);
                    getView().closeProgressBar();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

            getView().showToast(e.getMessage());
        }
    }

    public void onClickUpdateLocation() {
        MainView mView = getView();
        if (mView != null) {
            mView.checkLocation();
        }
    }

    public void onClickUnit() {
        if (isViewAttached() && getView() != null) {
            final View view = View.inflate(getView().getContext(), R.layout.unit_selector, null);
            final ToggleButton unitSwitch = (ToggleButton) view.findViewById(R.id.unit_switch);


            String title = getView().getContext().getString(R.string.unit_title);

            getView().showDialog(view, title, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    unit = unitSwitch.isChecked() ? TemperatureUnit.CELCII :TemperatureUnit.KELVIN;
                    if (getView()!= null) {
                        getView().getPreferences()
                                .edit()
                                .putBoolean(UNIT,unit.toBool())
                                .commit();
                    }

                    getView().setUnit(unit);
                    getView().updateList();
                    dialog.cancel();
                }
            });
        }
    }

    public void onClickSetCapacity() {
        if (isViewAttached() && getView() != null) {

            View view = View.inflate(getView().getContext(), R.layout.capacity_picker, null);
            final NumberPicker picker = (NumberPicker) view.findViewById(R.id.numberPicker);
            picker.setMaxValue(9);
            picker.setMinValue(1);
            picker.setValue(capacity);

            String title = getView().getContext().getString(R.string.capacity_title);

            getView().showDialog(view, title, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    capacity = picker.getValue();
                    if (getView()!= null) {
                        getView().getPreferences()
                                .edit()
                                .putInt(CAPACITY,capacity)
                                .commit();
                    }
                    getFromDB();
                    dialog.cancel();
                }
            });
        }
    }

    public void onRefresh() {
        if (currLocation != null) {

            getWeatherByLocale();
        } else {
            if (getView() != null) {
                getView().checkLocation();
            }
        }
    }

    public String getTitle() {
        return place.getCity();
    }

    private void puToDB(WeatherResponse response) {
        try {
            Statistic statistic = new Statistic(
                    response.getTime(),
                    response.getTemperature(),
                    response.getClouds(),
                    response.getWindSpeed(),
                    place);

            DBHelper.getInstance().getPlaceDao().create(place);
            DBHelper.getInstance().getStatisticDao().create(statistic);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getFromDB() {
        try {
            List<Statistic> list = DBHelper.getInstance().getStatisticDao().queryForAll();
            if (list != null) {
                for (Statistic statistic:list) {
                    DBHelper.getInstance().getPlaceDao().refresh(statistic.getPlace());
                }
                Collections.reverse(list);
                statisticList.clear();

                int i=0;
                for (Statistic statistic:list) {

                    if (capacity > i) {
                        statisticList.add(statistic);
                    } else {
                        DBHelper.getInstance().getStatisticDao().delete(statistic);
                    }
                    i++;
                }

                if (isViewAttached() && getView()!= null) {
                    getView().setUnit(unit);
                      getView().updateList();
                }
            }
        } catch (SQLException e){

            e.printStackTrace();
        }
    }

    private boolean isInternetConnected() {
        if (getView()!= null) {
            ConnectivityManager cm = (ConnectivityManager) getView().getContext().getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            return ni != null;

        }
        return false;
    }

    class WeatherCallBack implements Callback<JsonObject> {

        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            WeatherResponse weatherResponse = new Gson().fromJson(response.body(),WeatherResponse.class);
            puToDB(weatherResponse);
            getFromDB();
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            if (isViewAttached() && getView() != null) {
                getView().showToast(t.getMessage());
                getView().updateList();
            }
        }
    }
}
