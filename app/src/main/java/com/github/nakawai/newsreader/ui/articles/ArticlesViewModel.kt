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
            _articles.value = appService.loadNewsFeed(section, force)
            _isLoading.value = false
        }
    }


    @Suppress("UNCHECKED_CAST")
    class Factory(private val appService: StoryAppService) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ArticlesViewModel(appService) as T
        }
    }
}
