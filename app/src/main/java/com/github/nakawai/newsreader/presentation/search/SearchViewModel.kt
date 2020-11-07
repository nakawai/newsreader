package com.github.nakawai.newsreader.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.nakawai.newsreader.domain.model.NYTimesModel
import com.github.nakawai.newsreader.presentation.PresentationTranslator
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel(
    private val model: NYTimesModel
) : ViewModel() {
    private val _articles = MutableLiveData<List<SearchResultUiModel>>()
    val articles: LiveData<List<SearchResultUiModel>> = _articles
    private var job: Job? = null
    fun onQueryTextSubmit(query: String) {
        job?.cancel()

        job = viewModelScope.launch {
            _articles.value = model.searchArticle(query)
                .map { PresentationTranslator.translate(it) }
        }
        job = null
    }
}

