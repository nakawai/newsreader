package com.github.nakawai.newsreader.presentation.articles

import androidx.lifecycle.*
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.domain.entities.Story
import com.github.nakawai.newsreader.domain.model.NYTimesModel
import kotlinx.coroutines.launch

/**
 * ViewModel class for controlling the Articles Activity
 */
class ArticlesViewModel(
    private val model: NYTimesModel
) : ViewModel() {
    private val _stories = MediatorLiveData<List<Story>>()

    val articles: LiveData<List<ArticleUiModel>> = MediatorLiveData<List<ArticleUiModel>>().apply {
        addSource(_stories) { stories ->
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
        _stories.addSource(model.observeArticlesBySection(section)) {
            _stories.value = it
        }
    }

    fun loadData(force: Boolean) {
        _isLoading.value = true

        viewModelScope.launch {
            runCatching {
                model.loadNewsFeed(section, force)
            }.onSuccess { stories ->
                _stories.value = stories
            }.onFailure { error ->
                _error.value = error
            }

            _isLoading.value = false
        }
    }

}
