package com.github.nakawai.newsreader.domain.model

import com.github.nakawai.newsreader.domain.datasource.AppLocalDataSource
import com.github.nakawai.newsreader.domain.datasource.NYTimesLocalDataSource
import com.github.nakawai.newsreader.domain.datasource.NYTimesRemoteDataSource
import com.github.nakawai.newsreader.domain.entities.Section
import io.mockk.*
import io.mockk.impl.annotations.MockK
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

    @MockK
    private lateinit var local: NYTimesLocalDataSource

    @MockK
    private lateinit var remote: NYTimesRemoteDataSource

    @MockK
    private lateinit var appLocal: AppLocalDataSource

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)

        MockKAnnotations.init(this)

        coEvery { local.readData(any())} returns emptyList()
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
            val model = NYTimesModelImpl(local, remote, appLocal)
            every { appLocal.canCallApi(any()) } returns false

            // Act
            model.loadNewsFeed(Section.HOME, forceReload = false)

            // Assert
            coVerify(exactly = 0) { remote.fetchTopStories(any()) }
            coVerify(exactly = 1) { local.readData(Section.HOME) }

            confirmVerified(local, remote)

        }

    }
}
