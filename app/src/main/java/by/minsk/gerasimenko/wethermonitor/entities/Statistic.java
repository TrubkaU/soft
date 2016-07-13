package by.minsk.gerasimenko.wethermonitor.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created 12.07.2016.
 */
@DatabaseTable(tableName = "statistic")
public class Statistic implements Serializable{
    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField
    private long time;
    @DatabaseField
    private double temperature;
    @DatabaseField
    private Integer cloudiness;
    @DatabaseField
    private String windSpeed;

    @DatabaseField(foreign = true)
    private Place place;


    public Statistic() {
    }

    public Statistic(long time, double temperature, Integer cloudiness, String windSpeed, Place place) {
        this.time = time;
        this.temperature = temperature;
        this.cloudiness = cloudiness;
        this.windSpeed = windSpeed;
        this.place = place;
    }

    public long getTime() {
        return time;
    }

    public double getTemperature() {
        return temperature;
    }

    public Integer getCloudiness() {
        return cloudiness;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public Long getId() {
        return id;
    }

    public Place getPlace() {
        return place;
    }


}
