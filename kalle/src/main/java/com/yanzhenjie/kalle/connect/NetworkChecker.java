/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.kalle.connect;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

import static com.yanzhenjie.kalle.connect.NetworkChecker.NetType.Mobile;
import static com.yanzhenjie.kalle.connect.NetworkChecker.NetType.Mobile2G;
import static com.yanzhenjie.kalle.connect.NetworkChecker.NetType.Mobile3G;
import static com.yanzhenjie.kalle.connect.NetworkChecker.NetType.Mobile4G;
import static com.yanzhenjie.kalle.connect.NetworkChecker.NetType.Wifi;
import static com.yanzhenjie.kalle.connect.NetworkChecker.NetType.Wired;

/**
 * Created by Zhenjie Yan on 2018/2/13.
 */
public class NetworkChecker {

    private ConnectivityManager mManager;

    public NetworkChecker(Context context) {
        this.mManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    private static boolean isConnected(NetType netType, NetworkInfo networkInfo) {
        if (networkInfo == null) return false;

        switch (netType) {
            case Wifi: {
                if (!isConnected(networkInfo)) return false;
                return networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            }
            case Wired: {
                if (!isConnected(networkInfo)) return false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
                    return networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET;
                return false;
            }
            case Mobile: {
                if (!isConnected(networkInfo)) return false;
                return networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
            case Mobile2G: {
                if (!isConnected(Mobile, networkInfo)) return false;
                return isMobileSubType(Mobile2G, networkInfo);
            }
            case Mobile3G: {
                if (!isConnected(Mobile, networkInfo)) return false;
                return isMobileSubType(Mobile3G, networkInfo);
            }
            case Mobile4G: {
                if (!isConnected(Mobile, networkInfo)) return false;
                return isMobileSubType(Mobile4G, networkInfo);
            }
        }
        return false;
    }

    /**
     * Whether network connection.
     */
    private static boolean isConnected(NetworkInfo networkInfo) {
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    private static boolean isMobileSubType(NetType netType, NetworkInfo networkInfo) {
        switch (networkInfo.getType()) {
            case TelephonyManager.NETWORK_TYPE_GSM:
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN: {
                return netType == Mobile2G;
            }
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP: {
                return netType == Mobile3G;
            }
            case TelephonyManager.NETWORK_TYPE_IWLAN:
            case TelephonyManager.NETWORK_TYPE_LTE: {
                return netType == Mobile4G;
            }
            default: {
                String subtypeName = networkInfo.getSubtypeName();
                if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                        || subtypeName.equalsIgnoreCase("WCDMA")
                        || subtypeName.equalsIgnoreCase("CDMA2000")) {
                    return netType == Mobile3G;
                }
                break;
            }
        }
        return false;
    }

    /**
     * Check the network is enable.
     */
    public boolean isAvailable() {
        return isWifiConnected() || isWiredConnected() || isMobileConnected();
    }

    /**
     * To determine whether a WiFi network is available.
     */
    public final boolean isWifiConnected() {
        return isAvailable(Wifi);
    }

    /**
     * To determine whether a wired network is available.
     */
    public final boolean isWiredConnected() {
        return isAvailable(Wired);
    }

    /**
     * Mobile Internet connection.
     */
    public final boolean isMobileConnected() {
        return isAvailable(Mobile);
    }

    /**
     * 2G Mobile Internet connection.
     */
    public final boolean isMobile2GConnected() {
        return isAvailable(Mobile2G);
    }

    /**
     * 3G Mobile Internet connection.
     */
    public final boolean isMobile3GConnected() {
        return isAvailable(Mobile3G);
    }

    /**
     * 4G Mobile Internet connection.
     */
    public final boolean isMobile4GConnected() {
        return isAvailable(Mobile4G);
    }

    /**
     * According to the different type of network to determine whether the network connection.
     */
    public final boolean isAvailable(NetType netType) {
        return isConnected(netType, mManager.getActiveNetworkInfo());
    }

    public enum NetType {
        Wifi,
        Wired,
        Mobile,
        Mobile2G,
        Mobile3G,
        Mobile4G
    }
}