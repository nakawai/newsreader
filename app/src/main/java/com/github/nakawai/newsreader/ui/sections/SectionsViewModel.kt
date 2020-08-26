package com.github.nakawai.newsreader.ui.sections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.nakawai.newsreader.domain.NewsReaderAppService
import com.github.nakawai.newsreader.domain.entity.Section

/**
 * Presenter class for controlling the Main Activity
 */
class SectionsViewModel(private val appService: NewsReaderAppService) : ViewModel() {

    private val _sections = MutableLiveData<List<Section>>()
    val sections: LiveData<List<Section>> = _sections

    fun start() {
        _sections.value = appService.loadSections()
    }
}
