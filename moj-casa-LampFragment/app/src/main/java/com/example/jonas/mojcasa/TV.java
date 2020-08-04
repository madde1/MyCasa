package com.example.jonas.mojcasa;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Jonas on 2018-03-31.
 */

public class TV extends Fragment {

    private static final String TAG = MainActivity.class.getName();
    //String urlSmallTV = "http://192.168.0.5:1925/";
    String urlSmallTV = "http://192.168.0.50:1925/";
    String urlBigTV = "http://192.168.0.50:1925/";
    private static final Map<String, String> channelList = new LinkedHashMap<String, String>();
    static {
        channelList.put("0-1-2804-0-0-0-0", "SVT1");
        channelList.put("0-1-2916-0-0-0-0", "SVT2");
        channelList.put("0-1-3364-0-0-0-0", "TV3");
        channelList.put("0-1-3140-0-0-0-0", "4");
        channelList.put("0-1-2468-0-0-0-0", "Kanal5");
        channelList.put("0-1-3028-0-0-0-0", "6");
        channelList.put("0-1-3476-0-0-0-0", "7");
        channelList.put("0-1-4372-0-0-0-0", "8");
        channelList.put("0-1-4484-0-0-0-0", "9");
        channelList.put("0-1-3812-0-0-0-0", "10");
        channelList.put("0-1-4148-0-0-0-0", "11");
        channelList.put("0-1-4036-0-0-0-0", "12");
        channelList.put("0-1-3924-0-0-0-0", "Fox");
        channelList.put("0-1-2580-0-0-0-0", "TLC");
        channelList.put("0-1-2356-0-0-0-0", "Kunskapskanalen");
        channelList.put("0-1-4260-0-0-0-0", "Barnkanalen");
    }


    private Switch swTVMute;
    private Button btnTVUpdate;
    private Button btnTVRight;
    private Button btnTVLeft;
    private Button btnTVUp;
    private Button btnTVDown;
    private Button btnTV1;
    private Button btnTV2;
    private Button btnTV3;
    private Button btnTV4;
    private Button btnTV5;
    private Button btnTV6;
    private Button btnTV7;
    private Button btnTV8;
    private Button btnTV9;
    private Button btnTV0;
    private Button btnTVSource;
    private Button btnTVHome;
    private Button btnTVInfo;
    private Button btnTVFormat;
    private Button btnTVBack;
    private Button btnTVTV;



    private TextView txtVolume;
    private TextView txtChannel;
    private boolean boolMuted = false;

    private String strCurrentChannelID = "";
    private String strCurrentChannel = "Channel";
    private String strCurrentVolume = "Volume";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.tv, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("TV");
        setupButtons();
        setupText();
        getCurrentChannel();
        getVolumeInfo();
        updateTextViews();
    }

    private void setChannel(String channelID) {
        String url = urlSmallTV + "1/channels/current";

        JSONObject js = new JSONObject();
        try {
            js.put("id", channelID);
            Log.i(TAG, "Packat JSON format:");
            Log.i(TAG, js.toString());

        } catch (JSONException e) {
            Log.i(TAG, "JSONException i setChannel");
            e.printStackTrace();
        }
        RequestHandler.getInstance(getActivity().getApplicationContext()).postRequest(url, js);
        strCurrentChannelID = channelID;
        strCurrentChannel = channelList.get(channelID);
        updateTextViews();
    }

    private void setNextChannel() {
        String nextChannelID = "";
        String key;
        boolean foundKey = false;

        if (strCurrentChannelID != "") {
            for (Map.Entry<String, String> entry : channelList.entrySet()) {
                key = entry.getKey();
                if (foundKey) {
                    nextChannelID = key;
                    break;
                }
                if (key.contains(strCurrentChannelID)) {
                    foundKey = true;
                }
            }
            setChannel(nextChannelID);
        }
    }

    private void setPreviousChannel() {
        String previousChannelID = "";
        String key;

        if (strCurrentChannelID != "") {
            for (Map.Entry<String, String> entry : channelList.entrySet()) {
                key = entry.getKey();
                if (key.contains(strCurrentChannelID)) {
                    break;
                }
                previousChannelID = key;
            }
            setChannel(previousChannelID);
        }
    }

    private void getCurrentChannel() {
     /*   String url = urlSmallTV + "1/channels/current";
        String jsonResponse = RequestHandler.getInstance(getActivity().getApplicationContext()).getRequest(url);
        if (jsonResponse == null) {
            for (int i = 0; i < 2; i++) {
                jsonResponse = RequestHandler.getInstance(getActivity().getApplicationContext()).getRequest(url);
                if (jsonResponse != null) {
                    break;
                }
            }
        }
        if (jsonResponse != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonResponse);
                String currentChannelID = jsonObj.getString("id");
                Log.i(TAG, "currentChannelID: " + currentChannelID);
                strCurrentChannelID = currentChannelID;
                strCurrentChannel = channelList.get(strCurrentChannelID);
            } catch (JSONException e) {
                Log.i(TAG, "getCurrentChannelID: Error in JSON: " + e.toString());
            }
        }*/
        updateTextViews();
    }

    private void getVolumeInfo() {
       /* String url = urlSmallTV + "1/audio/volume";
        String jsonResponse = RequestHandler.getInstance(getActivity().getApplicationContext()).getRequest(url);
        if (jsonResponse == null) {
            for (int i = 0; i < 2; i++) {
                jsonResponse = RequestHandler.getInstance(getActivity().getApplicationContext()).getRequest(url);
                if (jsonResponse != null) {
                    break;
                }
            }
        }
        if (jsonResponse != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonResponse);
                strCurrentVolume = jsonObj.getString("current");
                if (jsonObj.getString("muted") == "true") boolMuted = true;
                else boolMuted = false;
            } catch (JSONException e) {
                Log.i(TAG, "Error in JSON: " + e.toString());
            }
        }*/
        updateTextViews();
    }

    private void setVolume(String volume, boolean mute) {
        String url = urlSmallTV + "1/audio/volume";

        JSONObject js = new JSONObject();
        try {
            js.put("current", volume);
            js.put("muted", mute);
            Log.i(TAG, "Packat JSON format:");
            Log.i(TAG, js.toString());

        } catch (JSONException e) {
            Log.i(TAG, "JSONException i setChannel");
            e.printStackTrace();
        }
        RequestHandler.getInstance(getActivity().getApplicationContext()).postRequest(url, js);
        strCurrentVolume = volume;
        updateTextViews();
    }

    private void volumeUp() {
        try {
            int intCurrentVolume = Integer.parseInt(strCurrentVolume);
            setVolume(Integer.toString(intCurrentVolume + 1), boolMuted);
        }
        catch (Exception e) {
            getVolumeInfo();
        }
    }

    private void volumeDown() {
        try {
            int intCurrentVolume = Integer.parseInt(strCurrentVolume);
            setVolume(Integer.toString(intCurrentVolume - 1), boolMuted);
        }
        catch (Exception e) {
            getVolumeInfo();
        }
    }

    private void mute() {
        try {
            boolMuted = !boolMuted;
            setVolume(strCurrentVolume, boolMuted);
        }
        catch (Exception e) {
            getVolumeInfo();
        }
    }

    private void tvKeyBtnPressed(String key, boolean menuMode, boolean switchMode) {
        String url = urlSmallTV + "6/input/key";
        JSONObject js = new JSONObject();
        try {
            js.put("key", key);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestHandler.getInstance(getActivity().getApplicationContext()).postRequest(url, js);
        if (switchMode) {
            setupNavigatorButtons(menuMode);
        }
    }

    private void updateTextViews() {
        txtChannel.setText(strCurrentChannel);
        txtVolume.setText(strCurrentVolume);
        swTVMute.setChecked(boolMuted);
    }

    private void setupNavigatorButtons(boolean menuMode) {
        if (menuMode) {
            btnTVUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvKeyBtnPressed("Confirm", true, false);
                }
            });
            btnTVRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvKeyBtnPressed("CursorUp", true, false);
                }
            });
            btnTVLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvKeyBtnPressed("CursorDown", true, false);
                }
            });
            btnTVUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvKeyBtnPressed("CursorRight", true, false);
                }
            });
            btnTVDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvKeyBtnPressed("CursorLeft", true, false);
                }
            });
        }
        else {
            btnTVUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getCurrentChannel();
                    getVolumeInfo();
                    updateTextViews();
                }
            });
            btnTVRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setNextChannel();
                }
            });
            btnTVLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setPreviousChannel();
                }
            });
            btnTVUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    volumeUp();
                }
            });
            btnTVDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    volumeDown();
                }
            });
        }
    }

    private void setupButtons() {
        swTVMute = getActivity().findViewById(R.id.swTVMute);
        btnTVUpdate = getActivity().findViewById(R.id.btnTVUpdate);
        btnTVRight = getActivity().findViewById(R.id.btnTVRight);
        btnTVLeft = getActivity().findViewById(R.id.btnTVLeft);
        btnTVUp = getActivity().findViewById(R.id.btnTVUp);
        btnTVDown = getActivity().findViewById(R.id.btnTVDown);
        btnTVSource = getActivity().findViewById(R.id.btnTVSource);
        btnTVHome = getActivity().findViewById(R.id.btnTVHome);
        btnTVInfo = getActivity().findViewById(R.id.btnTVInfo);
        btnTVFormat = getActivity().findViewById(R.id.btnTVFormat);
        btnTVBack = getActivity().findViewById(R.id.btnTVBack);
        btnTVTV = getActivity().findViewById(R.id.btnTVTV);
        btnTV1 = getActivity().findViewById(R.id.btnTV1);
        btnTV2 = getActivity().findViewById(R.id.btnTV2);
        btnTV3 = getActivity().findViewById(R.id.btnTV3);
        btnTV4 = getActivity().findViewById(R.id.btnTV4);
        btnTV5 = getActivity().findViewById(R.id.btnTV5);
        btnTV6 = getActivity().findViewById(R.id.btnTV6);
        btnTV7 = getActivity().findViewById(R.id.btnTV7);
        btnTV8 = getActivity().findViewById(R.id.btnTV8);
        btnTV9 = getActivity().findViewById(R.id.btnTV9);
        btnTV0 = getActivity().findViewById(R.id.btnTV0);

        swTVMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mute();
            }
        });

        btnTVSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvKeyBtnPressed("Source", true, true);
            }
        });
        btnTVHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvKeyBtnPressed("Home", true, true);
            }
        });
        btnTVInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvKeyBtnPressed("Info", true, true);
            }
        });

        btnTVFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvKeyBtnPressed("Viewmode", true, true);
            }
        });

        btnTVBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvKeyBtnPressed("Back", true, true);
            }
        });
        btnTVTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvKeyBtnPressed("WatchTV", false, true);
            }
        });

        btnTV1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvKeyBtnPressed("Digit1", false, false);
            }
        });
        btnTV2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvKeyBtnPressed("Digit2", false, false);
            }
        });
        btnTV3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvKeyBtnPressed("Digit3", false, false);
            }
        });
        btnTV4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvKeyBtnPressed("Digit4", false, false);
            }
        });
        btnTV5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvKeyBtnPressed("Digit5", false, false);
            }
        });
        btnTV6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvKeyBtnPressed("Digit6", false, false);
            }
        });
        btnTV7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvKeyBtnPressed("Digit7", false, false);
            }
        });
        btnTV8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvKeyBtnPressed("Digit8", false, false);
            }
        });
        btnTV9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvKeyBtnPressed("Digit9", false, false);
            }
        });
        btnTV0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvKeyBtnPressed("Digit0", false, false);
            }
        });

        setupNavigatorButtons(false);
    }
    private void setupText() {
        txtChannel = getActivity().findViewById(R.id.txtCurrentChannel);
        txtChannel.setText(strCurrentChannel);
        txtVolume = getActivity().findViewById(R.id.txtCurrentVolume);
        txtVolume.setText(strCurrentVolume);
    }
}
