package com.hyperkani.gifs_app.data.remote

import com.hyperkani.gifs_app.data.model.Gif
import com.hyperkani.gifs_app.data.model.GifResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GifsApi {

    @GET("gifs/search/")
    suspend fun getGifs(
        @Query("key") apiKey: String = KEY,
        @Query("q") q: Int = 2,
        @Query("limit") limit: Int = 25,
        @Query("offset") offset: Int = 0,
        @Query("rating") rating: String = "g",
        @Query("lang") lang: String = "en",
    ): GifResponse


    companion object {
        const val BASE_URL = "https://api.giphy.com/v1/"
        const val KEY = "18FmCjYOBZxlrF8naaZgpx1utNVMDjIt"
    }
}