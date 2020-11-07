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
class NYTimesRemoteDataSourceImplAndroidTest {

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
            val remote = NYTimesRemoteDataSourceImpl()

            // Act
            val stories = remote.fetchTopStories(Section.HOME)

            // Assert
            assertThat(stories).isNotEmpty()

        }
    }

    @Test
    fun testSearch() {
        runBlocking {
            // Arrange
            val remote = NYTimesRemoteDataSourceImpl()

            // Act
            val articles = remote.searchArticle("election")

            // Assert
            assertThat(articles).isNotEmpty()

        }
    }
}
