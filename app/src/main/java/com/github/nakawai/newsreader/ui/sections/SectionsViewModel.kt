package com.github.nakawai.newsreader.ui.sections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.nakawai.newsreader.data.Repository
import com.github.nakawai.newsreader.domain.story.Section

/**
 * Presenter class for controlling the Main Activity
 */
class SectionsViewModel(private val repository: Repository) : ViewModel() {

    private val _sections = MutableLiveData<List<Section>>()
    val sections: LiveData<List<Section>> = _sections

    fun start() {
        _sections.value = repository.loadSections()
    }
}
