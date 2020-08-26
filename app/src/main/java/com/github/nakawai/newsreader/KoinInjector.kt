package com.github.nakawai.newsreader

import com.github.nakawai.newsreader.data.Repository
import com.github.nakawai.newsreader.domain.StoryAppService
import com.github.nakawai.newsreader.ui.articles.ArticlesViewModel
import com.github.nakawai.newsreader.ui.details.DetailsViewModel
import com.github.nakawai.newsreader.ui.sections.SectionsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Repository() }
    single { StoryAppService(get()) }
    viewModel { SectionsViewModel(get()) }
    viewModel { ArticlesViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
}
