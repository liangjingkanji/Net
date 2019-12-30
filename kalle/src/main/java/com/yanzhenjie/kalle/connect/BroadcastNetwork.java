/*
 * Copyright © 2018 Zhenjie Yan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.kalle.connect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

/**
 * Created by Zhenjie Yan on 2018/2/20.
 */
public class BroadcastNetwork implements Network {

    private final Context mContext;
    private final NetworkReceiver mReceiver;

    public BroadcastNetwork(Context context) {
        this.mContext = context.getApplicationContext();
        this.mReceiver = new NetworkReceiver(new NetworkChecker(mContext));

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction("android.net.ethernet.STATE_CHANGE");
        filter.addAction("android.net.ethernet.ETHERNET_STATE_CHANGED");
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        mContext.registerReceiver(mReceiver, filter);
    }

    @Override
    public boolean isAvailable() {
        return mReceiver.mAvailable;
    }

    public void destroy() {
        mContext.unregisterReceiver(mReceiver);
    }

    private static class NetworkReceiver extends BroadcastReceiver {

        private NetworkChecker mChecker;
        private boolean mAvailable;

        public NetworkReceiver(NetworkChecker checker) {
            this.mChecker = checker;
            this.mAvailable = mChecker.isAvailable();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            mAvailable = mChecker.isAvailable();
        }
    }
}