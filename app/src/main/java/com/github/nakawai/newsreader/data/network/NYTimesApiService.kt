package com.github.nakawai.newsreader.data.network

import com.github.nakawai.newsreader.data.network.response.articlesearch.ArticleSearchResponse
import com.github.nakawai.newsreader.data.network.response.topstories.TopStoriesResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit interface for the New York Times WebService
 *
 * @see <a href="https://developer.nytimes.com/apis">APIs | Dev Portal</a>
 */
interface NYTimesApiService {
    @GET("svc/topstories/v2/{section}.json")
    suspend fun topStories(
        @Path("section") section: String,
        @Query(value = "api-key", encoded = true) apiKey: String
    ): Response<TopStoriesResponse>

    @GET("svc/topstories/v2/{section}.json")
    fun callTopStories(
        @Path("section") section: String,
        @Query(value = "api-key", encoded = true) apiKey: String
    ): Call<TopStoriesResponse>

    @GET("svc/search/v2/articlesearch.json")
    suspend fun articleSearch(
        @Query("query") query: String,
        @Query(value = "api-key", encoded = true) apiKey: String
    ): Response<ArticleSearchResponse>
}
