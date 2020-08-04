package com.example.jonas.mojcasa;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jonas.mojcasa.weather.ForecastAdapter;
import com.example.jonas.mojcasa.weather.WeatherPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;

public class WeatherAcitivty  extends Fragment {
    private static final String TAG = MainActivity.class.getName();
    private String API_KEY = "021525ec25b5af313e6e1ec65f39ecec";
    private String urlHourly = "http://api.openweathermap.org/data/2.5/forecast?q=Göteborg&units=metric&appid=" + API_KEY;
    private String urlDaily= "https://api.openweathermap.org/data/2.5/onecall?lat=57.7072&lon=11.9668&exclude=hourly,minutely,current&units=metric&appid=" + API_KEY;
    private RecyclerView mRecyclerView;
    ForecastAdapter mForecastAdapter = new ForecastAdapter();
    WeatherPreferences weatherPreferences = new WeatherPreferences();

    String [] parsedWeatherDate  = null ;
    String [] parsedWeatherDescription = null;
    String [] parsedWeatherTemp = null;
    String [] parsedWeatherTempLow = null;
    int [] parsedWeatherid = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.weather_activity, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Väder");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_forecast);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mForecastAdapter);

        updateWeatherInfo();



    }


    public void updateWeatherInfo() {
        RequestHandler.getInstance(getContext().getApplicationContext()).getRequest(urlDaily, new ResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(Object response) {
                    try {
                        response = new JSONObject(String.valueOf(response));
                        getWeatherJSONDaily((JSONObject) response);
                        loadWeatherDate();
                        loadWeatherDescription();
                        loadWeatherTemp();
                        loadWeatherTempLow();
                        loadWeatherid();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        });

    }

    public void getWeatherJSONHourly(JSONObject response){
        final String OWM_LIST = "list";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_FEELSLIKE = "feels_like";
        final String OWM_WEATHER = "weather";
        final String OWM_DESCRIPTION = "description";
        final String OWN_MAIN = "main";
        String [] jsonArrayData = new String[40];
       // response = new JSONObject(String.valueOf(response));
        try {

            JSONArray array = response.getJSONArray(OWM_LIST);

            for(int i = 0; i < array.length(); i++){

                    double high;
                    double feelsLike;
                    String description;

                    JSONObject getobj = array.getJSONObject(i);

                    JSONObject weatherData = getobj.getJSONObject(OWN_MAIN);

                    high = weatherData.getDouble(OWM_TEMPERATURE);
                    feelsLike = weatherData.getDouble(OWM_FEELSLIKE);

                    String temps = weatherPreferences.changeToNoDecimal(getActivity(),high);
                    String lowTemp = weatherPreferences.changeToNoDecimal(getActivity(),feelsLike);


                    JSONObject weatherObject =
                            getobj.getJSONArray(OWM_WEATHER).getJSONObject(0);
                    description = weatherObject.getString(OWM_DESCRIPTION);

                    int test = getobj.getInt("dt");
                    System.out.println(test);

                    String date = getDate(test);

                    jsonArrayData[i] = (date + " " + description + "  " + temps + "  " + lowTemp);


            }
            parsedWeatherDate = Arrays.copyOf(jsonArrayData, jsonArrayData.length);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void getWeatherJSONDaily(JSONObject response){
        final String OWM_DAILY = "daily";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MIN = "min";
        final String OWM_WEATHER = "weather";
        final String OWM_DESCRIPTION = "description";
        final String OWN_DAY = "day";
        final String OWM_WEATHER_ID = "id";

        String [] jsonDateArray = new String[8];
        String [] jsonDescriptionArray = new String[8];
        String [] jsonTempArray = new String[8];
        String [] jsonTempLowArray = new String[8];
        int [] jsonidArray = new int[8];
        // response = new JSONObject(String.valueOf(response));
        try {

            JSONArray array = response.getJSONArray(OWM_DAILY);

            for(int i = 0; i < array.length(); i++){
                int id;
                double high;
                double min;
                String description;

                JSONObject getobj = array.getJSONObject(i);

                JSONObject weatherData = getobj.getJSONObject(OWM_TEMPERATURE);

                high = weatherData.getDouble(OWN_DAY);
                min = weatherData.getDouble(OWM_MIN);

                String temps = weatherPreferences.changeToNoDecimal(getActivity(),high);
                String lowTemp = weatherPreferences.changeToNoDecimal(getActivity(),min);


                JSONObject weatherObject =
                        getobj.getJSONArray(OWM_WEATHER).getJSONObject(0);
                description = weatherObject.getString(OWM_DESCRIPTION);
                id = weatherObject.getInt(OWM_WEATHER_ID);

                int test = getobj.getInt("dt");
                System.out.println(test);

                String date = getDate(test);

                jsonDateArray[i] = date ;
                jsonDescriptionArray[i] = description;
                jsonTempArray[i] = temps;
                jsonTempLowArray[i] = lowTemp;
                jsonidArray[i] =id;

            }

            parsedWeatherDate = Arrays.copyOf(jsonDateArray, jsonDateArray.length);
            parsedWeatherDescription = Arrays.copyOf(jsonDescriptionArray, jsonDescriptionArray.length);
            parsedWeatherTemp = Arrays.copyOf(jsonTempArray, jsonTempArray.length);
            parsedWeatherTempLow = Arrays.copyOf(jsonTempLowArray, jsonTempLowArray.length);
            parsedWeatherid = Arrays.copyOf(jsonidArray, jsonidArray.length);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    public String getDate(int i){
        String dateOut;

        java.util.Date time=new java.util.Date((long)i*1000);

        SimpleDateFormat format1 = new SimpleDateFormat("EEE MMM dd");

        dateOut = format1.format(time);

        return dateOut;
    }

    private void loadWeatherDate(){
        mForecastAdapter.setWeatherDate(parsedWeatherDate);

    }
    private void loadWeatherDescription(){
        mForecastAdapter.setWeatherDescription(parsedWeatherDescription);
    }
    private void loadWeatherTemp(){
        mForecastAdapter.setWeatherTemp(parsedWeatherTemp);
    }
    private void loadWeatherTempLow(){
        mForecastAdapter.setWeatherTempLow(parsedWeatherTempLow);
    }
    private void loadWeatherid(){
        mForecastAdapter.setWeatherid(parsedWeatherid);
    }

    private void showWeatherDataView(){
        mRecyclerView.setVisibility(View.VISIBLE);
    }


}






