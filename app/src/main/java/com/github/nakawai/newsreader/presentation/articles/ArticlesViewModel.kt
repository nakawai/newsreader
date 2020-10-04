package com.github.nakawai.newsreader.presentation.articles

import androidx.lifecycle.*
import com.github.nakawai.newsreader.domain.model.NYTimesModel
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.domain.entities.Story
import kotlinx.coroutines.launch

/**
 * Presenter class for controlling the Main Activity
 */
class ArticlesViewModel(
    private val model: NYTimesModel
) : ViewModel() {
    private val _stories = MediatorLiveData<List<Story>>()
    val stories: LiveData<List<Story>> = _stories

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
            }.onSuccess {
                _stories.value = it
            }.onFailure {
                _error.value = it
            }

            _isLoading.value = false
        }
    }

}
