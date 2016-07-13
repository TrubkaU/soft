package by.minsk.gerasimenko.wethermonitor.entities;

/**
 * Created 13.07.2016.
 */
public enum TemperatureUnit  {
    KELVIN("K"),
    CELCII("C");
    private String text;

    TemperatureUnit(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public boolean toBool(){
        return this.equals(CELCII);
    }
}
