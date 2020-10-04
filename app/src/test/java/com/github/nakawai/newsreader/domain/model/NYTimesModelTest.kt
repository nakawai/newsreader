package com.github.nakawai.newsreader.domain.model

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

/**
 * @see <a href="https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/">kotlinx-coroutines-test</a>
 */
@ExperimentalCoroutinesApi
class NYTimesModelTest {

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
            val repo = NYTimesModelImpl(mockk(), mockk())

            // Act
            //repo.loadNewsFeed(Section.HOME, forceReload = false)

            //val actual = repo.timeSinceLastNetworkRequest(Section.HOME)

            //assertThat(actual).isEqualTo()
        }

    }
}