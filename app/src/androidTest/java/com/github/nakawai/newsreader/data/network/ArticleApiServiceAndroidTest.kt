package com.github.nakawai.newsreader.data.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.nakawai.newsreader.domain.entities.Section
import com.google.common.truth.Truth.assertThat

import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArticleApiServiceAndroidTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testFetchData() {
        runBlocking {
            // Arrange
            val service = ArticleRemoteDataSourceImpl().nyTimesApiService

            val response = service.topStories(Section.HOME.value)

            assertThat(response.isSuccessful).isEqualTo(true)

            val body = response.body()!!

            assertThat(body.results).isNotEmpty()
            assertThat(body.results!![0].publishedDate).isEqualTo("aaaaa")


        }
    }

}
