package com.github.nakawai.newsreader.data.network


import com.facebook.stetho.okhttp3.StethoInterceptor
import com.github.nakawai.newsreader.data.network.response.StoryResponseItem
import com.github.nakawai.newsreader.data.toData
import com.github.nakawai.newsreader.domain.story.Section
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import timber.log.Timber

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

    suspend fun fetchData(section: Section): List<StoryResponseItem> = withContext(Dispatchers.IO) {
        val sectionKey = section.toData().value
        val response = nyTimesApiService.topStories(sectionKey, API_KEY)

        if (response.isSuccessful) {
            Timber.d("Success - Data received: %s", sectionKey)
            return@withContext response.body()!!.results!!
        } else {
            Timber.d("Failure: Data not loaded: %s", sectionKey)
            throw RuntimeException()
        }
    }

    companion object {
        private const val API_KEY = "YUPmyj0Q09Fm2VlCHmD9FU7rpCcI5dUD"
    }


}
