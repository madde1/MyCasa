package com.example.jonas.mojcasa;

import android.util.Log;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LampViewModel extends ViewModel {

    private final static String TAG = "LampViewModel";

    private Lamps lamps;
    private MutableLiveData<List<Lamp>> lampLiveData;

    public LiveData<List<Lamp>> loadLampLiveData() {
        if (lampLiveData == null) {
            lampLiveData = new MutableLiveData<>();
            lamps = new Lamps();
        }
        getAllLampsState();
        return lampLiveData;
    }

    private void getAllLampsState() {
        lamps.updateAllLamps(new ResponseListener() {
            @Override
            public void onResponse(Object response) {
                lampLiveData.postValue(lamps.getLamps());
            }
            @Override
            public void onError(String message) {
                Log.i(TAG, String.format("Error in loadLamps: %s", message));
            }
        });
    }

    public void toggleAllLamps() {
        lamps.toggleAllLamps();
    }

//    void doAction() {
//        // depending on the action, do necessary business logic calls and update the
//        // lampLiveData.
//    }
}
