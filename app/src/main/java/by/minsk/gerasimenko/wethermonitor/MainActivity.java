package by.minsk.gerasimenko.wethermonitor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import butterknife.Bind;
import butterknife.ButterKnife;
import by.minsk.gerasimenko.wethermonitor.adapter.WeatherAdapter;
import by.minsk.gerasimenko.wethermonitor.entities.TemperatureUnit;
import by.minsk.gerasimenko.wethermonitor.mvp.MainPresenter;
import by.minsk.gerasimenko.wethermonitor.mvp.MainView;

public class MainActivity extends MvpActivity<MainView, MainPresenter> implements MainView, SwipyRefreshLayout.OnRefreshListener {

    @Bind(R.id.main_wether_recycler)
    RecyclerView recyclerWeather;
    @Bind(R.id.main_swipe_layout)
    SwipyRefreshLayout mSwipe;

    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            presenter.setCurrLocation(location);
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            lm.removeUpdates(locationListener);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        WeatherAdapter adapter = new WeatherAdapter(presenter.getStatisticList(), this);
        recyclerWeather.setAdapter(adapter);
        recyclerWeather.setLayoutManager(new LinearLayoutManager(this));


        mSwipe.setOnRefreshListener(this);
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }


    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_update:
                presenter.onClickUpdateLocation();
                break;
            case R.id.main_menu_capacity:
                presenter.onClickSetCapacity();
                break;
            case R.id.main_menu_units:
                presenter.onClickUnit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateList() {
        mSwipe.setRefreshing(false);
        recyclerWeather.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void checkLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, locationListener);
        } else if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, locationListener);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.err_title)
                    .setMessage(R.string.err_message)
                    .setPositiveButton(R.string.positive_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //MainActivity.this.finish();
                        }
                    }).show();
        }
    }

    @Override
    public void initActionBar(String city) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            if (!TextUtils.isEmpty(city)) {
                actionBar.setTitle(city);
            }
        }
    }

    @Override
    public void showToast(String text) {
        if (!TextUtils.isEmpty(text)) {
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void closeProgressBar() {
        mSwipe.setRefreshing(false);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {

        presenter.onRefresh();
    }

    @Override
    public void showDialog(View view,String title,DialogInterface.OnClickListener listener) {

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(view)
                .setPositiveButton(R.string.positive_btn, listener)
                .setNegativeButton(R.string.negative_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    @Override
    public SharedPreferences getPreferences() {
        return getPreferences(MODE_PRIVATE);
    }
    @Override
    public void setUnit(TemperatureUnit unit) {
        ((WeatherAdapter)recyclerWeather.getAdapter()).setUnit(unit);
    }
}
