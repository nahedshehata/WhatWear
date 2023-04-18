package com.example.whatwear

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.whatwear.databinding.ActivityMainBinding
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.net.URL

private const val BASE_URL =
    "https://api.tomorrow.io/v4/timelines?location=40.75872069597532,-73.98529171943665&fields=temperature&timesteps=1h&units=metric&apikey=uynWiHdTpa5UZQ2nPGbusm9hJHQRQp4y"

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    lateinit var binding: ActivityMainBinding
   private lateinit var client: OkHttpClient
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var myEdit: SharedPreferences.Editor
    var winterArr = listOf(R.drawable.sweet1, R.drawable.sweet2, R.drawable.bamb)
    var summerArr =
        listOf(R.drawable.dress1, R.drawable.dress2, R.drawable.dress3, R.drawable.drees4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        client = OkHttpClient()
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        myEdit = sharedPreferences.edit()
        myEdit.putInt("summer", 0)
        myEdit.putInt("winter", 0)
        myEdit.commit()
        binding.btnWear.setOnClickListener {
            makeRequest(BASE_URL)
        }
    }

    private fun makeRequest(sUrl: String): WeatherResponse? {
        var result: WeatherResponse? = null
        try {
            val url = URL(sUrl)

            val request = Request.Builder().url(url).build()

            val response = client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e(TAG, "onFailure: ${e.localizedMessage}")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        result = response.body?.string()?.toKotlinObject<WeatherResponse>()
                        val temperature =
                            result?.data?.timelines?.get(0)?.intervals?.get(0)?.values?.temperature
                        Log.d(
                            TAG,
                            "makeRequest: temperature: $temperature"
                        )
                        if (response.isSuccessful) {
                            runOnUiThread {
                                binding.tvTemperatureValue.text = "$temperature°C"

                                temperature?.let {
                                    if (temperature > 25) {
                                        var index = sharedPreferences.getInt("summer", 0)
                                        binding.ivWear.setImageResource(summerArr[index])
                                        index++
                                        if (index > 3)
                                            index = 0
                                        myEdit.putInt("summer", index)
                                        myEdit.commit()

                                    } else {
                                        var index = sharedPreferences.getInt("winter", 0)
                                        binding.ivWear.setImageResource(winterArr[index])
                                        index++
                                        if (index > 2)
                                            index = 0
                                        myEdit.putInt("winter", index)
                                        myEdit.commit()
                                    }
                                }
                            }
                        }
                    }
                })
        } catch (err: Error) {
            print("Error when executing get request: " + err.localizedMessage)
        }
        return result
    }

    inline fun <reified T : Any> String.toKotlinObject(): T =
        Gson().fromJson(this, T::class.java)

}

