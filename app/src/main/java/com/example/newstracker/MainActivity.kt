    package com.example.newstracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newstracker.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

    class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch(Dispatchers.IO) {
            RetrofitInstance.api.getBreakingNews("general", "ph", 1, "en")
                .body()?.let{
                    println(it.totalResults)
                }
        }
    }
}