package com.example.newstracker.repository

import com.example.newstracker.retrofit.RetrofitInstance
import com.example.newstracker.retrofit.dataclass.NewsResponse
import retrofit2.Response

class Repository {
    suspend fun repositoryArticles(category : String,
                                   country : String,
                                   pageNumber : Int,
                                   sources : String,
                                   language : String
    ): Response<NewsResponse> = RetrofitInstance.api.getBreakingNews(category, country, pageNumber, sources, language)

}