package com.github.nakawai.newsreader.ui.articles

import androidx.lifecycle.*
import com.github.nakawai.newsreader.domain.StoryAppService
import com.github.nakawai.newsreader.domain.story.Section
import com.github.nakawai.newsreader.domain.story.Story
import kotlinx.coroutines.launch

/**
 * Presenter class for controlling the Main Activity
 */
class ArticlesViewModel(
    private val appService: StoryAppService
) : ViewModel() {
    private val _articles = MediatorLiveData<List<Story>>()
    val storiesData: LiveData<List<Story>> = _articles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    private lateinit var section: Section

    fun start(section: Section) {
        this.section = section
        _articles.addSource(appService.observeArticlesBySection(section)) {
            _articles.value = it
        }
    }

    fun loadData(force: Boolean) {
        _isLoading.value = true

        viewModelScope.launch {
            runCatching {
                appService.loadNewsFeed(section, force)
            }.onSuccess {
                _articles.value = it
            }.onFailure {
                _error.value = it
            }

            _isLoading.value = false
        }
    }

}
