/*
 * Copyright 2016 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.nakawai.newsreader.model.network

import android.annotation.SuppressLint
import com.github.nakawai.newsreader.model.entity.NYTimesStory
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

    suspend fun loadData(
        sectionKey: String,
        apiKey: String
    ): List<NYTimesStory> {
        return loadNextSection(sectionKey, apiKey)
    }

    // Load all sections one by one.
    @SuppressLint("CheckResult")
    private suspend fun loadNextSection(sectionKey: String, apiKey: String): List<NYTimesStory> = suspendCoroutine { continuation ->

        nyTimesService.topStories(sectionKey, apiKey)
            .enqueue(object : Callback<NYTimesResponse<List<NYTimesStory>>> {
                override fun onFailure(call: Call<NYTimesResponse<List<NYTimesStory>>>, t: Throwable) {
                    Timber.d("Failure: Data not loaded: %s - %s", sectionKey, t.toString())
                    continuation.resumeWithException(t)
                }

                override fun onResponse(call: Call<NYTimesResponse<List<NYTimesStory>>>, response: Response<NYTimesResponse<List<NYTimesStory>>>) {
                    Timber.d("Success - Data received: %s", sectionKey)
                    continuation.resume(response.body()!!.results!!)
                }

            })

    }


}
