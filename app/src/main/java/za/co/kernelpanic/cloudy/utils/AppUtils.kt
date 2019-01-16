package za.co.kernelpanic.cloudy.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class AppUtils {

    companion object {

        fun checkConnectivity(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo: NetworkInfo = cm.activeNetworkInfo
            return activeNetworkInfo.isConnected
        }
    }
}