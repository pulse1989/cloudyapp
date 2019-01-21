package za.co.kernelpanic.cloudy.ui.fragments.Forecast

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import za.co.kernelpanic.cloudy.R
import za.co.kernelpanic.cloudy.data.ForecastResponse
import za.co.kernelpanic.cloudy.databinding.FragmentWeatherForecastBinding
import za.co.kernelpanic.cloudy.di.modules.WeatherViewModelFactory
import za.co.kernelpanic.cloudy.utils.AppUtils
import za.co.kernelpanic.cloudy.utils.WeatherUtils
import javax.inject.Inject


class WeatherFragment : Fragment() {

    private lateinit var binding: FragmentWeatherForecastBinding
    //we need to check for network connectivity before trying to get data

    /*
     * Our loading indicator
     */
    private var pendingProgress: ProgressBar? = null
    /*
     * We then inject our two special lads here in order to make this work,
     */
    @Inject
    lateinit var viewModelFactory: WeatherViewModelFactory

    @Inject
    lateinit var viewModel: WeatherForecastViewModel

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /*
         * In order for our business logic layer to respect this UI lifecycle, we bind our viewmodel to the UI.
         * This way even during configuration and other state changes. the app will be fine.
         */
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WeatherForecastViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment using databinding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather_forecast, container, false)
        pendingProgress = binding.appProgressIndicator
        setHasOptionsMenu(true)
        AppUtils.checkConnectivity(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
    }

    /*
         * Show a loading screen while we retrieve data.
         */
    private fun showLoading() {
        pendingProgress?.visibility = View.VISIBLE
        binding.imgCurrentWeather.visibility = View.INVISIBLE
        getData()
    }

    /*
     * Internet connection has been confirmed to be working, lets try and grab the user data
     * If this is unsuccessful, something else beyond our control is most likely down
     */
    private fun getData() {
        viewModel.weather.observe(this, Observer {
            if (it != null && !it.forecastList.isNullOrEmpty()) {
                hideLoading()
                updateUI(it)
            } else {
                showSnackBar("Unable to get Weather Information. Try again later")
                hideLoading()
            }
        })
    }

    private fun hideLoading() {
        pendingProgress?.visibility = View.INVISIBLE
        binding.imgCurrentWeather.visibility = View.VISIBLE
    }

    private fun updateUI(weatherInfo: ForecastResponse) {
        /*
         * Weather Icon to set for the app
         */
        val weatherId = weatherInfo.forecastList[0].weather[0].iconId!!
        val weatherImageId = WeatherUtils.getLargeCurrentWeatherArt(weatherId)
        /*
         * Location specific info
         */
        val weatherLocation = weatherInfo.city.name
        val weatherDescription = weatherInfo.forecastList[0].weather[0].description
        /*
         * Our core temperature info:
         */
        val temperature = weatherInfo.forecastList[0].temperature.dayForecast!!
        val highTemp = weatherInfo.forecastList[0].temperature.maxTemp!!
        val lowTemp = weatherInfo.forecastList[0].temperature.minTemp!!
        val currentTemp = WeatherUtils.formatTemperature(activity!!, temperature)
        val maxTemp = WeatherUtils.formatTemperature(activity!!, highTemp)
        val minTemp = WeatherUtils.formatTemperature(activity!!, lowTemp)
        /*
         * Pressure and humidity
         */
        val pressure = weatherInfo.forecastList[0].pressure
        val humidity = weatherInfo.forecastList[0].humidity
        val humidityString = getString(R.string.format_humidity, humidity)
        val pressureString = getString(R.string.format_pressure, pressure)
        /*
         * Wind information
         */
        val windSpeed = weatherInfo.forecastList[0].windSpeed
        val windDirection = weatherInfo.forecastList[0].windDirection
        val windSpeedString = WeatherUtils.getFormattedWind(activity, windSpeed, windDirection)
        //we now have to bind all our awesome variables to each of their views so we can see the weather
        binding.tvLocationHeader.text = weatherLocation
        binding.tvWeatherDescription.text = weatherDescription
        binding.tvWeatherTemp.text = currentTemp
        binding.tvMinTempDetail.text = minTemp
        binding.maxTempDetail.text = maxTemp
        binding.tvHumidityDetail.text = humidityString
        binding.tvAirPressureDetail.text = pressureString
        binding.tvWindSpeedDetail.text = windSpeedString
        binding.imgCurrentWeather.setImageResource(weatherImageId)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.main, menu)
    }
    /*
     * Whenever the refresh icon is tapped, this method is triggered.
     * As always, we check for network connectivity.
     */

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_refresh -> {
                showSnackBar("Refreshing...")
                AppUtils.checkConnectivity(context)
                Log.i(LOG_TAG, "Refreshing weather info")
                super.onOptionsItemSelected(item) //superclass will handle default options
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*
     * Default message method for showing information to the user.
     */
    private fun showSnackBar(message: String) {
        if (view != null) {
            Snackbar.make(view!!, message, Snackbar.LENGTH_LONG).show()
        } else {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onPause() {
        viewModel.removeLocationUpdates()
        super.onPause()
    }

    companion object {

        private val LOG_TAG = WeatherFragment::class.java.simpleName

        fun newInstance(): WeatherFragment {
            val args = Bundle()
            val fragment = WeatherFragment()
            fragment.arguments = args
            return fragment
        }
    }
}