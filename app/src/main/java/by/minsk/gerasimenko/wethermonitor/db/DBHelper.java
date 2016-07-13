package by.minsk.gerasimenko.wethermonitor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import by.minsk.gerasimenko.wethermonitor.entities.Place;
import by.minsk.gerasimenko.wethermonitor.entities.Statistic;

/**
 * Created 12.07.2016.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static DBHelper instance;


    private static final String TAG = DBHelper.class.getSimpleName();
    private static final String DATABASE_NAME ="weather.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Statistic, Long> statisticDao;
    private Dao<Place, Long> placeDao;

    public static void newInstance(Context context) {
        instance = OpenHelperManager.getHelper(context, DBHelper.class);
    }

    public static DBHelper getInstance() {
        return instance;
    }

    public DBHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        instance = this;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource){
        try {
            TableUtils.createTable(connectionSource, Statistic.class);
            TableUtils.createTable(connectionSource, Place.class);
        } catch (SQLException e) {
            Log.e(TAG, "error creating DB " + DATABASE_NAME);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer,
                          int newVer){
        try{
            TableUtils.dropTable(connectionSource, Statistic.class, true);
            TableUtils.dropTable(connectionSource, Place.class, true);
            onCreate(db, connectionSource);
        }
        catch (SQLException e){
            Log.e(TAG,"error upgrading db "+DATABASE_NAME+"from ver "+oldVer);
            throw new RuntimeException(e);
        }
    }

    public Dao<Place, Long> getPlaceDao() {
        if(placeDao == null) {
            try {
                placeDao = getDao(Place.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return placeDao;
    }

    public Dao<Statistic, Long> getStatisticDao() {
        if(statisticDao == null) {
            try {
                statisticDao = getDao(Statistic.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return statisticDao;
    }

    @Override
    public void close(){
        super.close();
        statisticDao = null;
        placeDao = null;
    }
}