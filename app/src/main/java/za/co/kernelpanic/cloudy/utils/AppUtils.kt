package za.co.kernelpanic.cloudy.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import za.co.kernelpanic.cloudy.BuildConfig

class AppUtils {

    companion object {

        /*
        * Let's make sure the user has a working internet connection before we attempt to get the weather data.
        * We save battery life by not invoking any location requests here. We also notify the user that they should check
        * their settings.
         */

        fun checkConnectivity(context: Context?): Boolean {
            val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo: NetworkInfo = cm.activeNetworkInfo
            return activeNetworkInfo.isConnected
        }

        fun logErrorInfo(logTag: String, logMessage: String) {
            if (BuildConfig.DEBUG) {
                Log.e(logTag, logMessage)
            }
        }

        fun logVerboseInfo(logTag: String, logMessage: String) {
            if (BuildConfig.DEBUG) {
                Log.v(logTag, logMessage)
            }
        }
    }
}