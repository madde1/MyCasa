package com.example.jonas.mojcasa;

import android.util.Log;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public class Lamps {

    private final String TAG = "Lamps";

    @SerializedName("switches")
    private List<Lamp> lamps;
    private String state = "on";

    private Retrofit retrofitClient;

    public List<Lamp> getLamps() {
        return lamps;
    }

    public String getState() {
        return state;
    }

    public boolean isPoweredOn() {
        return getState().equals("on");
    }

    public void toggleAllLamps() {
        if (isPoweredOn()) {
            toggleAllPower("off");
        } else {
            toggleAllPower("on");
        }
    }

    public void toggleAllPower(String newLampState) {
        retrofitClient = RetrofitClient.getClient(LampApi.BASE_URL);
        LampApi api = retrofitClient.create(LampApi.class);
        Call<ResponseBody> call = api.setAllSwitchesState(newLampState);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                assert response.body() != null;
                state = newLampState;
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(
                    TAG,
                    String.format("Failed to toggleAllPower %s: %s", newLampState, t.getMessage())
                );
            }
        });
    }

    public void updateAllLamps(ResponseListener listener) {
        retrofitClient = RetrofitClient.getClient(LampApi.BASE_URL);
        LampApi api = retrofitClient.create(LampApi.class);
        Call<Lamps> call = api.getSwitches();
        call.enqueue(new Callback<Lamps>() {
            @Override
            public void onResponse(Call<Lamps> call, Response<Lamps> response) {
                assert response.body() != null;
                lamps = response.body().lamps;
                listener.onResponse(response);
            }
            @Override
            public void onFailure(Call<Lamps> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });
    }
}

class Lamp {
    private final String TAG = "Lamp";
    @SerializedName("name")
    private String name;
    @SerializedName("value")
    private String state;

    private int id;
    private Retrofit retrofitClient;

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPoweredOn() {
        return getState().equals("on");
    }

    public void toggle() {
        if (isPoweredOn()) {
            togglePower("off");
        } else {
            togglePower("on");
        }
    }

    public void togglePower(String newLampState) {
        state = newLampState; // TODO: set state inside onResponse, GUI needs to change on event as well
        retrofitClient = RetrofitClient.getClient(LampApi.BASE_URL);
        LampApi api = retrofitClient.create(LampApi.class);
        Call<ResponseBody> call = api.setSwitchState(name, newLampState);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                assert response.body() != null;
//                state = newLampState;
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(
                    TAG,
                    String.format("Failed to togglePower %s for %s: %s", newLampState, name, t.getMessage())
                );
            }
        });
    }
}

class RetrofitClient {

    private static Retrofit retrofit = null;
//    private static Object api = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit;
    }

//    public static Object connectApi(Object apiClass) {
//        assert retrofit != null;
//        if (api != null) {
//            api = retrofit.create(apiClass);
//        }
//        return api;
//    }
}

interface LampApi {

    String BASE_URL = "https://graph-eu01-euwest1.api.smartthings.com/api/smartapps/installations/b3db46b0-1388-42ed-b149-74ad5390a452/";
    String SMARTTHINGS_TOKEN = "70024820-721b-4487-8db9-6a79c3d286c0";

    @Headers({
        "Content-Type: application/json;charset=utf-8",
        "Authorization: Bearer " + SMARTTHINGS_TOKEN
    })
    @GET("switches")
    Call<Lamps> getSwitches();

    @Headers({
        "Content-Type: application/json;charset=utf-8",
        "Authorization: Bearer " + SMARTTHINGS_TOKEN
    })
    @PUT("switches/{state}")
    Call<ResponseBody> setAllSwitchesState(@Path("state") String newState);

    @Headers({
        "Content-Type: application/json;charset=utf-8",
        "Authorization: Bearer " + SMARTTHINGS_TOKEN
    })
    @PUT("switches/{switch}/{state}")
    Call<ResponseBody> setSwitchState(
        @Path("switch") String lampName, @Path("state") String newState
    );
}
