package com.github.nakawai.newsreader.presentation.articles


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.domain.repository.ArticleRepository
import com.github.nakawai.newsreader.domain.repository.HistoryRepository
import com.github.nakawai.newsreader.util.CoroutinesTestRule
import com.github.nakawai.newsreader.util.observeForTesting
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


/**
 * [TopStoriesViewModel] Test
 */
class TopStoriesViewModelTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @MockK(relaxed = true)
    lateinit var articleRepository: ArticleRepository

    @MockK(relaxed = true)
    lateinit var historyRepository: HistoryRepository


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun start() {
        // Arrange
        val viewModel = TopStoriesViewModel(articleRepository, historyRepository)

        // Act
        viewModel.start(Section.HOME)

        // Assert
        viewModel.topStoryUiModels.observeForTesting {
            assertThat(viewModel.topStoryUiModels.value).isEqualTo(null)
        }
    }
}
