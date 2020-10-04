package com.github.nakawai.newsreader

import com.github.nakawai.newsreader.data.db.AppLocalDataSourceImpl
import com.github.nakawai.newsreader.domain.model.NYTimesModel
import com.github.nakawai.newsreader.domain.model.NYTimesModelImpl
import com.github.nakawai.newsreader.domain.datasource.NYTimesLocalDataSource
import com.github.nakawai.newsreader.data.db.NYTimesLocalDataSourceImpl
import com.github.nakawai.newsreader.domain.datasource.NYTimesRemoteDataSource
import com.github.nakawai.newsreader.data.network.NYTimesRemoteDataSourceImpl
import com.github.nakawai.newsreader.domain.datasource.AppLocalDataSource
import com.github.nakawai.newsreader.presentation.articles.ArticlesViewModel
import com.github.nakawai.newsreader.presentation.details.DetailsViewModel
import com.github.nakawai.newsreader.presentation.sections.SectionsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<NYTimesRemoteDataSource> { NYTimesRemoteDataSourceImpl() }
    single<NYTimesLocalDataSource> { NYTimesLocalDataSourceImpl() }
    single<AppLocalDataSource> { AppLocalDataSourceImpl() }
    single<NYTimesModel> { NYTimesModelImpl(get(), get(), get() ) }
    viewModel { SectionsViewModel(get()) }
    viewModel { ArticlesViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
}
