package com.example.newstracker.repository

import com.example.newstracker.retrofit.RetrofitInstance
import com.example.newstracker.retrofit.dataclass.NewsResponse
import retrofit2.Response

class RetrofitRepository {
    suspend fun repositoryArticles(category : String,
                                   country : String,
                                   keyword : String,
                                   language : String,
                                   pageSize : Int = 100
    ): Response<NewsResponse> = RetrofitInstance.api.getBreakingNews(category, country, keyword, pageSize, language)
}