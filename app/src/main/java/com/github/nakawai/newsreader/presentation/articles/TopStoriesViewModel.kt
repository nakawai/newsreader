package com.github.nakawai.newsreader.presentation.articles

import androidx.lifecycle.*
import com.github.nakawai.newsreader.domain.entities.Article
import com.github.nakawai.newsreader.domain.entities.History
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.domain.repository.ArticleRepository
import com.github.nakawai.newsreader.domain.repository.HistoryRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * ViewModel class for controlling the Articles Activity
 */
class TopStoriesViewModel @AssistedInject constructor(
    private val articleRepository: ArticleRepository,
    historyRepository: HistoryRepository,
    @Assisted private val section: Section
) : ViewModel() {
    private val _forceReload = MutableLiveData(false)

    private val _topStories = _forceReload.switchMap { forceReload ->
        viewModelScope.launch {
            _isLoading.value = true
            articleRepository.loadTopStoriesBySectionIfNeed(section, forceReload)
            _isLoading.value = false
        }

        articleRepository.observeArticlesBySection(section)
    }.asFlow()

    private val _histories = historyRepository.observeHistories()


    val topStoryUiModels: Flow<List<ArticleUiModel>> = combine(_topStories, _histories) { topStories, histories ->
        buildUiModels(topStories, histories)
    }

    private fun buildUiModels(articles: List<Article>, history: List<History>): List<ArticleUiModel> {
        return articles.map { article ->
            ArticleUiModel(article = article, System.currentTimeMillis(), history.find { it.url.value == article.url.value } != null)
        }
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    fun loadArticles() {
        _forceReload.value = false
    }

    fun refresh() {
        _forceReload.value = true
    }

    companion object {
        fun provideFactory(
            assistedFactory: TopStoriesViewModelAssistedFactory,
            section: Section
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(section) as T
            }
        }
    }

}

@AssistedFactory
interface TopStoriesViewModelAssistedFactory {
    fun create(section: Section): TopStoriesViewModel
}
