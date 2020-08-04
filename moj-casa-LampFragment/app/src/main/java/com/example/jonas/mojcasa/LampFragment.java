package com.example.jonas.mojcasa;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;


public class LampFragment extends Fragment {

    private static final String TAG = MainActivity.class.getName();

    private LampViewModel lampViewModel;

    private Button[] lampButtons = new Button[20];
    private LinearLayout llLampName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.lamp, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Lamp");
        setupButtons();
        llLampName = getView().findViewById(R.id.llLampName);
        lampViewModel = new ViewModelProvider(requireActivity()).get(LampViewModel.class);
        lampViewModel.loadLampLiveData().observe(getViewLifecycleOwner(), new Observer<List<Lamp>>() {
            @Override
            public void onChanged(List<Lamp> lamps) {
                llLampName.removeAllViews();
                for (int i = 0; i < lamps.size(); i++) {
                    final Lamp lamp = lamps.get(i);
                    lamp.setId(i);
                    lampButtons[i] = new Button(getContext());
                    lampButtons[i].setText(lamp.getName() + "   " + lamp.getState());
                    lampButtons[i].setId(lamp.getId());
                    lampButtons[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            lamp.toggle();
                            lampButtons[lamp.getId()].setText(lamp.getName() + "   " + lamp.getState());
                        }
                    });
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    llLampName.addView(lampButtons[i], lp);
                }
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
//        lampViewModel.loadLampLiveData();
    }

    private void setupButtons() {
        setupToggleAllButton();
        setupUpdateButton();
    }

    private void setupToggleAllButton() {
        Button btnSwitchAllLamps = getActivity().findViewById(R.id.btnSwitchAllLamps);
        btnSwitchAllLamps.setText("Toggle all lamps off");
        btnSwitchAllLamps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lampViewModel.toggleAllLamps();
                lampViewModel.loadLampLiveData();
            }
        });
    }

    private void setupUpdateButton() {
        Button btnLampsUpdate = getActivity().findViewById(R.id.btnLampsUpdate);
        btnLampsUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lampViewModel.loadLampLiveData();
            }
        });
    }
}
