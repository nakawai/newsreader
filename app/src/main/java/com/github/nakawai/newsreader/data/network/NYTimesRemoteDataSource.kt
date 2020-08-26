package com.github.nakawai.newsreader.data.network

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.github.nakawai.newsreader.data.network.response.StoryResponseItem
import com.github.nakawai.newsreader.data.network.response.TopStoriesResponse
import com.github.nakawai.newsreader.data.toData
import com.github.nakawai.newsreader.domain.story.Section
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Class that handles network requests for the New York Times API
 */
class NYTimesRemoteDataSource {
    private val nyTimesApiService: NYTimesApiService

    init {
        val okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create())
            .baseUrl("https://api.nytimes.com/")
            .client(okHttpClient)
            .build()

        nyTimesApiService = retrofit.create(NYTimesApiService::class.java)
    }

    suspend fun fetchData(section: Section): List<StoryResponseItem> {
        return suspendCoroutine { continuation ->
            val sectionKey = section.toData().value
            nyTimesApiService.topStories(sectionKey, API_KEY)
                .enqueue(object : Callback<TopStoriesResponse> {
                    override fun onResponse(call: Call<TopStoriesResponse>, response: Response<TopStoriesResponse>) {
                        Timber.d("Success - Data received: %s", sectionKey)
                        continuation.resume(response.body()!!.results!!)
                    }

                    override fun onFailure(call: Call<TopStoriesResponse>, t: Throwable) {
                        Timber.d("Failure: Data not loaded: %s - %s", sectionKey, t.toString())
                        continuation.resumeWithException(t)
                    }


                })

        }
    }

    companion object {
        private const val API_KEY = "YUPmyj0Q09Fm2VlCHmD9FU7rpCcI5dUD"
    }


}
