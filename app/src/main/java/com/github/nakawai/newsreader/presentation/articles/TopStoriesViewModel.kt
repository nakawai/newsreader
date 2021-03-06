package com.github.nakawai.newsreader.presentation.articles

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.github.nakawai.newsreader.domain.entities.Article
import com.github.nakawai.newsreader.domain.entities.History
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.domain.repository.ArticleRepository
import com.github.nakawai.newsreader.domain.repository.HistoryRepository
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel class for controlling the Articles Activity
 */
class TopStoriesViewModel @ViewModelInject constructor(
    private val articleRepository: ArticleRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {
    private val _section = MutableLiveData<Section>()

    private val _topStories = _section.switchMap { articleRepository.observeArticlesBySection(it) }

    private val _histories = historyRepository.observeHistories()

    val topStoryUiModels: LiveData<List<ArticleUiModel>> = MediatorLiveData<List<ArticleUiModel>>().also { mediator ->
        mediator.addSource(_topStories) { articles ->
            Timber.d("onChange articles")
            mediator.value = buildUiModels(articles, _histories.value.orEmpty().map { it })
        }

        mediator.addSource(historyRepository.observeHistories()) { histories ->
            Timber.d("onChange histories")
            mediator.value = buildUiModels(_topStories.value.orEmpty(), histories.map { it })
        }
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

    fun start(section: Section) {
        _section.value = section
    }

    fun refresh() {
        val section = _section.value ?: return
        _isLoading.value = true

        viewModelScope.launch {
            runCatching {
                articleRepository.updateTopStoriesBySection(section)
            }.onSuccess {
                // NOP
            }.onFailure { error ->
                _error.value = error
            }

            _isLoading.value = false
        }
    }

}
