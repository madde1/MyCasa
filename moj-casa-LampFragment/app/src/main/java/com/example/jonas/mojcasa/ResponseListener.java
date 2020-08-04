package com.example.jonas.mojcasa;

public interface ResponseListener {

    void onResponse(Object response);

    void onError(String message);
}
