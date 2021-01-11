package com.github.nakawai.newsreader.domain.repository

import com.github.nakawai.newsreader.domain.datasource.ArticleLocalDataSource
import com.github.nakawai.newsreader.domain.datasource.ArticleRemoteDataSource
import com.github.nakawai.newsreader.domain.datasource.ConfigLocalDataSource
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
class NYTimesModelImplTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI Thread")

    @MockK
    private lateinit var local: ArticleLocalDataSource

    @MockK
    private lateinit var remote: ArticleRemoteDataSource

    @MockK
    private lateinit var configLocal: ConfigLocalDataSource

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)

        MockKAnnotations.init(this)

        coEvery { local.readTopStoriesBySection(any()) } returns emptyList()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `FetchTopStories should NOT be called when canCallApi and forceReload are false`() {
        runBlocking {
            // Arrange
            val model = ArticleRepositoryImpl(local, remote, configLocal)
            every { configLocal.canCallApi(any()) } returns false

            // Act
            model.loadTopStoriesBySection(Section.HOME)

            // Assert
            coVerify(exactly = 0) { remote.fetchTopStories(any()) }
            coVerify(exactly = 1) { local.readTopStoriesBySection(Section.HOME) }

            confirmVerified(local, remote)

        }

    }
}
