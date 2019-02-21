package za.co.kernelpanic.cloudy.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import za.co.kernelpanic.cloudy.R
import za.co.kernelpanic.cloudy.databinding.ActivityMainBinding
import za.co.kernelpanic.cloudy.ui.fragments.Forecast.WeatherFragment
import za.co.kernelpanic.cloudy.utils.AppUtils
import javax.inject.Inject

class WeatherActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var locationRequest: LocationRequest

    private val LOG_TAG = WeatherActivity::class.java.simpleName
    private val CHECK_PLAY_SERVICES = 9000
    private val FINE_LOCATION_REQUEST_CODE = 8
    private val CHECK_DEVICE_SETTINGS = 8000

    private lateinit var settingsClient: SettingsClient
    private lateinit var locationSettingsRequest: LocationSettingsRequest
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        /*
         * we need this here before calling onCreate otherwise our fragment attachments can end up failing
         * as there is a possibility the fragment can get attached while the activity calles super().
         * Before that happens, our activity has to be injected! otherwise any fragments that rely on this activity can crash
         */
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        settingsClient = LocationServices.getSettingsClient(this)

        /*
         * Only if we haven't got anything in onSaveInstanceState and we've confirmed checkplay services is here.
         * we can go ahead and attach the fragment
         */
        if (savedInstanceState == null && checkPlayServices()) {
            requestPermission()
        }
    }

    private fun loadFragment() {
        val weatherFragment = WeatherFragment.newInstance()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction().add(binding.mainLayout.id, weatherFragment, "weather")
        transaction.commit()
    }

    /*
     * As we are using Google Play Services new Location API for this app, we have to ensure all dependencies are met.
     * This includes using the correct version of play services that is on the device. We take care of that here.
     */
    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val result = apiAvailability.isGooglePlayServicesAvailable(this)
        return if (result != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(result)) {
                apiAvailability.getErrorDialog(this, result, CHECK_PLAY_SERVICES).show()
            }
            AppUtils.logErrorInfo(LOG_TAG, getString(R.string.play_services_missing))
            false
        } else {
            AppUtils.logVerboseInfo(LOG_TAG, getString(R.string.play_services_installed))
            true
        }
    }

    override fun onResume() {
        super.onResume()
        if (!checkPermissions()) {
            requestPermission()
        }
    }

    /*
     * if this is the first time we're launching, let's ask the user for Location permissions before implementing this api. We need to do that here
     * before we can subscribe to location updates. We'll include a decent rationale as well to let them know this is so we get the correct weather
     * from their location
     */
    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (shouldProvideRationale) {
            AppUtils.logVerboseInfo(LOG_TAG, getString(R.string.location_permission_rationale))
            Snackbar.make(binding.mainLayout, getString(R.string.location_rationale),
                    Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.ok))
            {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        FINE_LOCATION_REQUEST_CODE)
            }.show()
        } else {
            Log.v(LOG_TAG, getString(R.string.requesting_permission_text))
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_LOCATION_REQUEST_CODE)
        }
    }

    /*
    * We handle our permission results here to ensure we have the correct results
    */

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            FINE_LOCATION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AppUtils.logVerboseInfo(LOG_TAG, getString(R.string.log_got_user_permissions))
                loadFragment()
                buildLocationSettingsRequest()
                checkDeviceSettings()
            }
        }
    }

    private fun buildLocationSettingsRequest() {
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        locationSettingsRequest = builder.build()
    }

    /*
     * here, we check if the device settings for the location are actually on. We may have permission, but the device needs
     * to accommodate this. We can make sure we check the settings here.
     * If the location settings aren't what we need them to be (HIGH accuracy) we first try to execute a PendingIntent to launch
     * the settings dialogue in order to resolve this. If the PendingIntent fails, the user has to resolve the location settings on their own.
     */

    private fun checkDeviceSettings() {
        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(this) { Log.i(LOG_TAG, getString(R.string.log_user_correct_settings)) }
                .addOnFailureListener(this) { e ->
                    val statusCode = (e as ApiException).statusCode
                    when (statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            Log.w(LOG_TAG, getString(R.string.log_get_required_settings))
                            try {
                                val rae = e as ResolvableApiException
                                rae.startResolutionForResult(this@WeatherActivity, CHECK_DEVICE_SETTINGS)

                            } catch (ise: IntentSender.SendIntentException) {

                                Log.w(LOG_TAG, getString(R.string.log_pending_intent_request))
                            }
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage = getString(R.string.log_pending_intent_failure)
                            Log.w(LOG_TAG, errorMessage)
                            Toast.makeText(this@WeatherActivity, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                }
    }

    /*
     * This will be done when the user has returned from the settings dialog.
     * RESULT = OK -> the user has made the change and we can continue with loading up the app.
     * otherwise, they have to resolve this manually
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CHECK_DEVICE_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> Log.i(LOG_TAG, "The user has agreed to make the settings change")
                Activity.RESULT_CANCELED -> Log.w(LOG_TAG, "The user cancelled request, they'll have to fix it manually")
            }
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return androidInjector
    }
}
