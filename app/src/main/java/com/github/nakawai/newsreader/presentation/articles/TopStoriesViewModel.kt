package com.github.nakawai.newsreader.presentation.articles

import androidx.lifecycle.*
import com.github.nakawai.newsreader.domain.entities.Article
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.domain.repository.ArticleRepository
import kotlinx.coroutines.launch

/**
 * ViewModel class for controlling the Articles Activity
 */
class TopStoriesViewModel(
    private val repository: ArticleRepository
) : ViewModel() {
    private val _topStories = MediatorLiveData<List<Article>>()

    val topStories: LiveData<List<ArticleUiModel>> = MediatorLiveData<List<ArticleUiModel>>().apply {
        addSource(_topStories) { stories ->
            value = stories.map { ArticleUiModel(it, System.currentTimeMillis()) }
        }
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    private lateinit var section: Section

    fun start(section: Section) {
        this.section = section
        _topStories.addSource(repository.observeArticlesBySection(section)) {
            _topStories.value = it
        }
    }

    fun loadData(force: Boolean) {
        _isLoading.value = true

        viewModelScope.launch {
            runCatching {
                repository.loadTopStoriesBySection(section, force)
            }.onSuccess { stories ->
                _topStories.value = stories
            }.onFailure { error ->
                _error.value = error
            }

            _isLoading.value = false
        }
    }

}
