package com.myhexaville.tictactoe;

import android.app.Application;

import com.facebook.FacebookSdk;

public class TicTacToeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(this);
    }
}
