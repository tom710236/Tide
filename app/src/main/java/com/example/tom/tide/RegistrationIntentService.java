package com.example.tom.tide;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by user on 2017/3/19.
 */

public class RegistrationIntentService extends IntentService {
    public RegistrationIntentService(){
        super("RegistrationIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(getString(R.string.gcm_sender_id),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.d("MyInstanceID", token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
