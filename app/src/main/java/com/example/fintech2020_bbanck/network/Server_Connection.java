package com.example.fintech2020_bbanck.network;

import android.content.Context;

public class Server_Connection {

    private String url;
    private Context context;

    public Server_Connection(String url, Context context) {
        this.context = context;
        this.url = "https://" + url;
    }

    public void connection() {
        SendRequest sendRequest = new SendRequest();
        sendRequest.send(url, 0, null, context);
    }

}
