package com.github.nakawai.newsreader.data.network


import androidx.annotation.VisibleForTesting
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.github.nakawai.newsreader.data.network.response.articlesearch.translate
import com.github.nakawai.newsreader.data.network.response.topstories.StoryResponseItem
import com.github.nakawai.newsreader.data.network.response.topstories.TopStoriesResponse
import com.github.nakawai.newsreader.data.network.response.topstories.translate
import com.github.nakawai.newsreader.data.network.retrofit.NYTimesApiService
import com.github.nakawai.newsreader.domain.datasource.ArticleRemoteDataSource
import com.github.nakawai.newsreader.domain.entities.Article
import com.github.nakawai.newsreader.domain.entities.Section
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Class that handles network requests for the New York Times API
 */
class ArticleRemoteDataSourceImpl : ArticleRemoteDataSource {
    @VisibleForTesting
    val nyTimesApiService: NYTimesApiService

    init {
        val okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .addNetworkInterceptor { chain ->
                var request = chain.request()
                val tag = request.tag(Invocation::class.java)
                if (tag != null && tag.method().getAnnotation(NYTimesApi::class.java) != null) {
                    request = request.newBuilder().url(
                        request.url().newBuilder().addQueryParameter("api-key", API_KEY).build()
                    ).build()
                }

                chain.proceed(request)
            }
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

    override suspend fun fetchTopStories(section: Section): List<Article> = withContext(Dispatchers.IO) {
        val sectionKey = section.value

        val response = nyTimesApiService.topStories(sectionKey)

        if (response.isSuccessful) {
            Timber.i("Success - Data received. section:${sectionKey} body:${response.body()}")
            return@withContext response.body()!!.results!!.map { it.translate(section) }
        } else {
            Timber.i("Failure: Data not loaded: section:${sectionKey}")
            throw RuntimeException()
        }
    }

    override suspend fun fetchTopStoriesWithCall(section: Section): List<StoryResponseItem> = suspendCoroutine { continuation ->
        val sectionKey = section.value
        val callTopStories = nyTimesApiService.callTopStories(sectionKey)

        callTopStories.enqueue(object : Callback<TopStoriesResponse> {
            override fun onResponse(call: Call<TopStoriesResponse>, response: Response<TopStoriesResponse>) {
                if (response.isSuccessful) {
                    Timber.i("Success - Data received. section:${sectionKey} body:${response.body()}")
                    continuation.resume(response.body()!!.results!!)
                } else {
                    Timber.i("Failure: Data not loaded: section:${sectionKey}")
                    continuation.resumeWithException(RuntimeException(""))
                }

            }

            override fun onFailure(call: Call<TopStoriesResponse>, t: Throwable) {
                continuation.resumeWithException(t)
            }
        })

        callTopStories.cancel()


    }

    override suspend fun searchArticle(query: String): List<Article> = withContext(Dispatchers.IO) {
        val response = nyTimesApiService.articleSearch(query)

        if (response.isSuccessful) {
            return@withContext response.body()!!.response!!.docs!!.map { it.translate() }
        } else {
            throw RuntimeException(response.errorBody()!!.string())
        }
    }

    companion object {
        private const val API_KEY = "YUPmyj0Q09Fm2VlCHmD9FU7rpCcI5dUD"
    }


}

annotation class NYTimesApi
