package com.example.newstracker.retrofit.`interface`

import com.example.newstracker.Constants.Companion.API_KEY
import com.example.newstracker.retrofit.dataclass.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("category")
        category : String = "",
        @Query("country")
        country : String = "ph",
        @Query("q")
        keyword : String,
        @Query("language")
        language : String = "en",
        @Query("apiKey")
        apiKey : String = API_KEY
    ) : Response<NewsResponse>
}