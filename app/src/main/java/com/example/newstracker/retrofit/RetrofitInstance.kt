package com.example.newstracker.retrofit

import com.example.newstracker.Constants.Companion.BASE_URL
import com.example.newstracker.retrofit.`interface`.NewsApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {

        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val api: NewsApi by lazy {
            retrofit.create(NewsApi::class.java)
        }
    }


}