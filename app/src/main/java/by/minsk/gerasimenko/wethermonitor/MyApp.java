package by.minsk.gerasimenko.wethermonitor;

import android.support.multidex.MultiDexApplication;

import by.minsk.gerasimenko.wethermonitor.db.DBHelper;

/**
 * Created 12.07.2016.
 */
public class MyApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        DBHelper.newInstance(getApplicationContext());
    }
}
