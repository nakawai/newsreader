package com.github.nakawai.newsreader.presentation.articles

import androidx.lifecycle.*
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.domain.repository.ArticleRepository
import kotlinx.coroutines.launch

/**
 * ViewModel class for controlling the Articles Activity
 */
class TopStoriesViewModel(
    private val repository: ArticleRepository
) : ViewModel() {
    private val _section = MutableLiveData<Section>()

    private val _topStories = _section.switchMap { repository.observeArticlesBySection(it) }

    val topStoryUiModels: LiveData<List<ArticleUiModel>> = _topStories.map { topStories ->
        topStories.map { ArticleUiModel(it, System.currentTimeMillis()) }
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    fun start(section: Section) {
        _section.value = section
    }

    fun loadData(force: Boolean) {
        val section = _section.value ?: return
        _isLoading.value = true

        viewModelScope.launch {
            runCatching {
                repository.loadTopStoriesBySection(section, force)
            }.onSuccess {
                // NOP
            }.onFailure { error ->
                _error.value = error
            }

            _isLoading.value = false
        }
    }

}
