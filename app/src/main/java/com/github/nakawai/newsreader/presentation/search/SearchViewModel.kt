package com.github.nakawai.newsreader.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.nakawai.newsreader.domain.model.NYTimesModel
import kotlinx.coroutines.launch

class SearchViewModel(
    private val model: NYTimesModel
) : ViewModel() {
    fun onQueryTextSubmit(query: String) {
        viewModelScope.launch {
            model.searchArticle(query)
        }
    }
}
