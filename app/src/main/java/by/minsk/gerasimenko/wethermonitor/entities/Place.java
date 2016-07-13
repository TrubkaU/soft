package by.minsk.gerasimenko.wethermonitor.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created 12.07.2016.
 */
@DatabaseTable(tableName = "places")
public class Place implements Serializable {

    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField
    private String country;
    @DatabaseField
    private String city;

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public Place() {
    }

    public Place(String country, String city) {
        this.country = country;
        this.city = city;
    }
}
