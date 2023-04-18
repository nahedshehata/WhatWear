package com.example.whatwear

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.whatwear.databinding.ActivityMainBinding
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.URL

private const val BASE_URL =
    "https://api.tomorrow.io/v4/timelines?location=40.75872069597532,-73.98529171943665&fields=temperature&timesteps=1h&units=metric&apikey=uynWiHdTpa5UZQ2nPGbusm9hJHQRQp4y"

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var client: OkHttpClient
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var myEdit: SharedPreferences.Editor
    private var winterArr = listOf(R.drawable.sweet1, R.drawable.sweet2, R.drawable.bamb)
    private var summerArr =
        listOf(R.drawable.dress1, R.drawable.dress2, R.drawable.dress3, R.drawable.drees4)
    private var moodArr= listOf(R.drawable.rain,R.drawable.sunwith,R.drawable.sun)

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
             client.newCall(request).enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                    Log.e(TAG, "onFailure: ${e.localizedMessage}")
                }
                override fun onResponse(call: Call, response: Response) {
                    result = response.body?.string()?.toKotlinObject<WeatherResponse>()
                    val temperature =
                        result?.data?.timelines?.get(0)?.intervals?.get(0)?.values?.temperature

                    if (response.isSuccessful) {
                        runOnUiThread {
                            binding.tvTemperatureValue.text = "$temperature°C"

                            temperature?.let {
                                if (temperature > 25) {
                                    summer()


                                } else {
                                    winter()
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
   private fun summer(){
        var index = sharedPreferences.getInt("summer", 0)
        binding.ivWear.setImageResource(summerArr[index])
       binding.ivMood.setImageResource(moodArr[2])
        index++
        if (index > 3)
            index = 0
        myEdit.putInt("summer", index)
        myEdit.commit()
    }
   private fun  winter(){
       var index = sharedPreferences.getInt("winter", 0)
       binding.ivWear.setImageResource(winterArr[index])
       binding.ivMood.setImageResource(moodArr[0])
       index++
       if (index > 2)
           index = 0
       myEdit.putInt("winter", index)
       myEdit.commit()
    }

    inline fun <reified T : Any> String.toKotlinObject(): T =
        Gson().fromJson(this, T::class.java)

}

