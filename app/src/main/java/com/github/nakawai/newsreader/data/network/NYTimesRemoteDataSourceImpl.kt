package com.github.nakawai.newsreader.data.network


import com.facebook.stetho.okhttp3.StethoInterceptor
import com.github.nakawai.newsreader.data.DataTranslator
import com.github.nakawai.newsreader.data.network.response.topstories.StoryResponseItem
import com.github.nakawai.newsreader.data.toData
import com.github.nakawai.newsreader.domain.datasource.NYTimesRemoteDataSource
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.domain.entities.Story
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://api.nytimes.com/")
            .client(okHttpClient)
            .build()

        nyTimesApiService = retrofit.create(NYTimesApiService::class.java)
    }

    override suspend fun fetchTopStories(section: Section): List<StoryResponseItem> = withContext(Dispatchers.IO) {
        val sectionKey = section.toData().value
        val response = nyTimesApiService.topStories(sectionKey, API_KEY)

        if (response.isSuccessful) {
            Timber.i("Success - Data received. section:${sectionKey} body:${response.body()}")
            return@withContext response.body()!!.results!!
        } else {
            Timber.i("Failure: Data not loaded: section:${sectionKey}")
            throw RuntimeException()
        }
    }

    override suspend fun searchArticle(query: String): List<Story> = withContext(Dispatchers.IO) {
        val response = nyTimesApiService.articleSearch(query, API_KEY)

        if (response.isSuccessful) {
            return@withContext response.body()!!.response!!.docs!!.map { DataTranslator.translate(it) }
        } else {
            throw RuntimeException(response.errorBody()!!.string())
        }
    }

    companion object {
        private const val API_KEY = "YUPmyj0Q09Fm2VlCHmD9FU7rpCcI5dUD"
    }


}
