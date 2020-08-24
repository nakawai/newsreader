package com.github.nakawai.newsreader.model.network

import com.github.nakawai.newsreader.model.entity.Section
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
    private val nyTimesService: NYTimesService

    init {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create())
            .baseUrl("https://api.nytimes.com/")
            .build()
        nyTimesService = retrofit.create(NYTimesService::class.java)
    }

    suspend fun fetchData(section: Section): List<NYTimesStoryResponseItem> {
        return suspendCoroutine { continuation ->

            nyTimesService.topStories(section.key, API_KEY)
                .enqueue(object : Callback<NYTimesResponse<List<NYTimesStoryResponseItem>>> {
                    override fun onFailure(call: Call<NYTimesResponse<List<NYTimesStoryResponseItem>>>, t: Throwable) {
                        Timber.d("Failure: Data not loaded: %s - %s", section.key, t.toString())
                        continuation.resumeWithException(t)
                    }

                    override fun onResponse(
                        call: Call<NYTimesResponse<List<NYTimesStoryResponseItem>>>,
                        response: Response<NYTimesResponse<List<NYTimesStoryResponseItem>>>
                    ) {
                        Timber.d("Success - Data received: %s", section.key)
                        continuation.resume(response.body()!!.results!!)
                    }

                })

        }
    }

    companion object {
        private const val API_KEY = "YUPmyj0Q09Fm2VlCHmD9FU7rpCcI5dUD"
    }


}
