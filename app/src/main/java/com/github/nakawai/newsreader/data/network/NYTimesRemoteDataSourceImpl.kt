package com.github.nakawai.newsreader.data.network


import com.facebook.stetho.okhttp3.StethoInterceptor
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.nakawai.newsreader.data.network.response.StoryResponseItem
import com.github.nakawai.newsreader.data.network.response.TopStoriesResponse
import com.github.nakawai.newsreader.data.toData
import com.github.nakawai.newsreader.domain.datasource.NYTimesRemoteDataSource
import com.github.nakawai.newsreader.domain.entities.Section
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import timber.log.Timber

/**
 * Class that handles network requests for the New York Times API
 */
class NYTimesRemoteDataSourceImpl : NYTimesRemoteDataSource {
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

    override suspend fun fetchTopStories(section: Section): List<StoryResponseItem> = withContext(Dispatchers.IO) {
        val sectionKey = section.toData().value
        val response = nyTimesApiService.topStories(sectionKey, API_KEY)

        if (response.isSuccessful) {
            Timber.i("Success - Data received. section:${sectionKey} body:${response.body().string()}")
            return@withContext response.body()!!.results!!
        } else {
            Timber.i("Failure: Data not loaded: section:${sectionKey}")
            throw RuntimeException()
        }
    }

    private fun TopStoriesResponse?.string(): String {
        return ObjectMapper().writeValueAsString(this)
    }

    companion object {
        private const val API_KEY = "YUPmyj0Q09Fm2VlCHmD9FU7rpCcI5dUD"
    }


}
