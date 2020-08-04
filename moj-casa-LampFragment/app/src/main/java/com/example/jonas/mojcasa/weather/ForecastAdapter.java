package com.example.jonas.mojcasa.weather;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jonas.mojcasa.R;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {
    private String[] forecastWeatherDate;
    private String [] forecastWeatherDescription;
    private String [] forecastWeatherTemp;
    private String [] forecastWeatherTempLow;
    private int [] forecastWeatherid;


    public ForecastAdapter(){

    }

    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder{
        final TextView dateView;
        final TextView descriptionView;
        final TextView highTempView;
        final TextView lowTempView;

        final ImageView iconView;

        public ForecastAdapterViewHolder(View view){
            super(view);
            iconView = (ImageView) view.findViewById(R.id.weather_icon);
            dateView = (TextView) view.findViewById(R.id.date);
            descriptionView = (TextView) view.findViewById(R.id.weather_description);
            highTempView = (TextView) view.findViewById(R.id.high_temperature);
            lowTempView = (TextView) view.findViewById(R.id.low_temperature);


        }
    }

    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.weather_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return  new ForecastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder forecastAdapterViewHolder, int position){
        int weatherid = forecastWeatherid[position];
        int weatherImageId = weatherid;
        weatherImageId = WeatherPreferences
                .getSmallArtResourceIdForWeatherCondition(weatherImageId);
        forecastAdapterViewHolder.iconView.setImageResource(weatherImageId);

       String weatherforthisDay = forecastWeatherDate[position];
        forecastAdapterViewHolder.dateView.setText(weatherforthisDay);

        String weatherdescription = forecastWeatherDescription[position];
        forecastAdapterViewHolder.descriptionView.setText(weatherdescription);

        String weatherTemp = forecastWeatherTemp[position];
        forecastAdapterViewHolder.highTempView.setText(weatherTemp);

        String weatherTempLow = forecastWeatherTempLow[position];
        forecastAdapterViewHolder.lowTempView.setText(weatherTempLow);
    }

    @Override
    public int getItemCount(){
       if(forecastWeatherDate == null) return 0;
        return forecastWeatherDate.length;
    }

    public void setWeatherDate(String[] weatherData){
        forecastWeatherDate = weatherData;
        notifyDataSetChanged();
    }
    public void setWeatherDescription(String[] weatherData){
        forecastWeatherDescription = weatherData;
        notifyDataSetChanged();
    }
    public void setWeatherTemp(String[] weatherData){
        forecastWeatherTemp = weatherData;
        notifyDataSetChanged();
    }
    public void setWeatherTempLow(String[] weatherData){
        forecastWeatherTempLow = weatherData;
        notifyDataSetChanged();
    }
    public void setWeatherid(int[] weatherData){
        forecastWeatherid = weatherData;
        notifyDataSetChanged();
    }

}
