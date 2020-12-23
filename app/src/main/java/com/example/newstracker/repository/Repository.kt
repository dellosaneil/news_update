package com.example.newstracker.repository

import com.example.newstracker.retrofit.RetrofitInstance
import com.example.newstracker.retrofit.dataclass.NewsResponse
import retrofit2.Response

class Repository {
    suspend fun retrieveArticles(): Response<NewsResponse> = RetrofitInstance.api.getBreakingNews()

}