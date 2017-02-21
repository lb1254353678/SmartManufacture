package com.goodbaby.push;

import android.app.Activity;
import android.os.Bundle;

import com.goodbaby.push.service.PollingService;
import com.goodbaby.push.util.PollingUtils;

/**
 * Created by goodbaby on 17/1/22.
 */

public class MainTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Start polling service
        System.out.println("Start polling service...");
        PollingUtils.startPollingService(this, 1, PollingService.class, PollingService.ACTION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Stop polling service
        System.out.println("Stop polling service...");
        PollingUtils.stopPollingService(this, PollingService.class, PollingService.ACTION);
    }

}

