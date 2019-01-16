package za.co.kernelpanic.cloudy.repository.remote

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import za.co.kernelpanic.cloudy.BuildConfig
import za.co.kernelpanic.cloudy.R
import za.co.kernelpanic.cloudy.data.ForecastResponse
import za.co.kernelpanic.cloudy.utils.WeatherConstants
import javax.inject.Inject

class WeatherNetworkDataSourceImpl @Inject constructor(private val context: Context, private val weatherApi: WeatherApi) : WeatherNetwork {

    private val logTag = WeatherNetworkDataSourceImpl::class.java.simpleName
    private val weatherDataStream = MutableLiveData<ForecastResponse>()

    override fun fetchWeatherForecast(latitude: Double, longitude: Double): LiveData<ForecastResponse> {

        val apiResponse: Call<ForecastResponse> = weatherApi.getForecast(WeatherConstants.API_KEY, latitude, longitude, WeatherConstants.REQUEST_MODE, WeatherConstants.REQUEST_UNITS, WeatherConstants.REQUEST_FORECAST_DAYS)
        apiResponse.enqueue(object : Callback<ForecastResponse> {

            override fun onResponse(call: Call<ForecastResponse>, response: Response<ForecastResponse>) {
                if (response.isSuccessful) {
                    weatherDataStream.value = response.body()
                } else {
                    when (response.code()) {
                        404 -> Toast.makeText(context, context.getText(R.string.response_code_404), Toast.LENGTH_LONG).show()
                        500 -> Toast.makeText(context, context.getText(R.string.response_code_500), Toast.LENGTH_LONG).show()
                        else -> Toast.makeText(context, context.getText(R.string.default_response_code_error), Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                if (BuildConfig.DEBUG) {
                    Log.e(logTag, context.getString(R.string.response_failure_log, "\n".plus(t.message)))
                    Toast.makeText(context, context.getString(R.string.response_failure_text), Toast.LENGTH_LONG).show()
                }
            }
        })
        return weatherDataStream
    }
}