package by.minsk.gerasimenko.wethermonitor.mvp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpView;

import by.minsk.gerasimenko.wethermonitor.entities.TemperatureUnit;

/**
 * Created 11.07.2016.
 */
public interface MainView extends MvpView {

    void updateList();
    void checkLocation();
    void initActionBar(String city);
    void showToast(String text);
    void closeProgressBar();
    void showDialog(View view, String title, DialogInterface.OnClickListener listener);
    void setUnit(TemperatureUnit unit);

    SharedPreferences getPreferences();

    Context getContext();
}
