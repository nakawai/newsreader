package com.github.nakawai.newsreader.presentation.articles


import android.text.format.DateUtils
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.github.nakawai.newsreader.domain.entities.Article
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.domain.repository.ArticleRepository
import com.github.nakawai.newsreader.domain.repository.HistoryRepository
import com.github.nakawai.newsreader.util.CoroutinesTestRule
import com.github.nakawai.newsreader.util.observeForTesting
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
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

    @MockK(relaxed = false)
    lateinit var articleRepository: ArticleRepository

    @MockK(relaxed = false)
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
        mockkStatic(DateUtils::class)
        every {
            DateUtils.getRelativeTimeSpanString(
                any(), any(), ofType(Long::class)
            )
        } returns "test"
        // Arrange
        every { historyRepository.observeHistories() } returns MutableLiveData()
        val mutableLiveData = MutableLiveData<List<Article>>()
        every { articleRepository.observeArticlesBySection(Section.HOME) } returns mutableLiveData

        val viewModel = TopStoriesViewModel(articleRepository, historyRepository)

        // Act
        viewModel.loadArticles(Section.HOME)
        mutableLiveData.value = listOf(mockk(relaxed = true))

        // Assert
        viewModel.topStoryUiModels.observeForTesting {
            assertThat(viewModel.topStoryUiModels.value!!).hasSize(1)
        }
    }
}
