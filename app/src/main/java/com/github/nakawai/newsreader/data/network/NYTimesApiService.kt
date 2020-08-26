package com.github.nakawai.newsreader.data.network

import com.github.nakawai.newsreader.data.network.response.TopStoriesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit interface for the New York Times WebService
 */
interface NYTimesApiService {
    @GET("svc/topstories/v2/{section}.json")
    fun topStories(
        @Path("section") section: String,
        @Query(value = "api-key", encoded = true) apiKey: String
    ): Call<TopStoriesResponse>
}
