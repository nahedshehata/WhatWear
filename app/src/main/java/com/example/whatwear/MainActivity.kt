package com.example.whatwear

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.whatwear.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnWear.setOnClickListener {
            makeRequest()
        }
    }

    private fun makeRequest() {
        TODO("Not yet implemented")
    }
}