package com.github.nakawai.newsreader.data

import com.github.nakawai.newsreader.domain.story.Section
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RepositoryTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI Thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun testLoadNewsFeed() {
        runBlocking {
            // Arrange
            val repo = RepositoryImpl(mockk(), mockk())

            // Act
            //repo.loadNewsFeed(Section.HOME, forceReload = false)

            //val actual = repo.timeSinceLastNetworkRequest(Section.HOME)

            //assertThat(actual).isEqualTo()
        }

    }
}
