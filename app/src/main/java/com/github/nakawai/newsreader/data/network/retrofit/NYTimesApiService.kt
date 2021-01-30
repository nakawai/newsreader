package com.github.nakawai.newsreader.data.network.retrofit

import com.github.nakawai.newsreader.data.network.NYTimesApi
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
    @NYTimesApi
    @GET("svc/topstories/v2/{section}.json")
    suspend fun topStories(
        @Path("section") section: String
    ): Response<TopStoriesResponse>

    @NYTimesApi
    @GET("svc/topstories/v2/{section}.json")
    fun callTopStories(
        @Path("section") section: String
    ): Call<TopStoriesResponse>

    @NYTimesApi
    @GET("svc/search/v2/articlesearch.json")
    suspend fun articleSearch(
        @Query("query") query: String
    ): Response<ArticleSearchResponse>
}
