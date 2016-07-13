package by.minsk.gerasimenko.wethermonitor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import by.minsk.gerasimenko.wethermonitor.Const;
import by.minsk.gerasimenko.wethermonitor.R;
import by.minsk.gerasimenko.wethermonitor.entities.Place;
import by.minsk.gerasimenko.wethermonitor.entities.Statistic;
import by.minsk.gerasimenko.wethermonitor.entities.TemperatureUnit;

/**
 * Created 11.07.2016.
 */
public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Statistic> statisticList;
    Context mContext;
    SimpleDateFormat format;
    TemperatureUnit unit = TemperatureUnit.KELVIN;

    public WeatherAdapter(List<Statistic> statisticList, Context mContext) {
        this.statisticList = statisticList;
        this.mContext = mContext;
        this.format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",new Locale(Const.LOCATION));
    }


    public void setUnit(TemperatureUnit unit) {
        this.unit = unit;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_weather,parent,false);
        return new WeatherHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        bindWeather((WeatherHolder) holder, statisticList.get(position));
    }

    private void bindWeather(WeatherHolder holder,Statistic statistic) {

        Place place = statistic.getPlace();
        if (place != null) {
            holder.tvCountry.setText(place.getCountry());
            holder.tvCity.setText(place.getCity());
        }

        Date date = new Date(statistic.getTime()*1000);
        String text = format.format(date);
        holder.tvDate.setText(text);

        StringBuilder builder = new StringBuilder();
        builder.append(toString(statistic.getTemperature()))
                .append(" ")
                .append(unit.toString());
        holder.tvTemperature.setText(builder);

        holder.tvCloudness.setText(String.valueOf(statistic.getCloudiness()));

        builder.setLength(0);
        builder.append(statistic.getWindSpeed())
                .append(" ")
                .append(mContext.getString(R.string.unit_wind_speed));
        holder.tvWindSpeed.setText(builder);
    }

    @Override
    public int getItemCount() {
        if (statisticList != null) return statisticList.size();
        return 0;
    }

    private String toString(double kelvin){
        switch (unit) {
            case KELVIN:
                default:
                return String.valueOf(kelvin);
            case CELCII:
                return String.valueOf(kelvin - 273.15);
        }
    }

    static class WeatherHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_weather_country)
        TextView tvCountry;
        @Bind(R.id.item_weather_city)
        TextView tvCity;
        @Bind(R.id.item_weather_date)
        TextView tvDate;

        @Bind(R.id.item_weather_temperature)
        TextView tvTemperature;
        @Bind(R.id.item_weather_cloudiness)
        TextView tvCloudness;
        @Bind(R.id.item_weather_wind_speed)
        TextView tvWindSpeed;

        public WeatherHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
