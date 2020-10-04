package com.github.nakawai.newsreader

import com.github.nakawai.newsreader.data.Repository
import com.github.nakawai.newsreader.data.RepositoryImpl
import com.github.nakawai.newsreader.data.db.NYTimesLocalDataSource
import com.github.nakawai.newsreader.data.db.NYTimesLocalDataSourceImpl
import com.github.nakawai.newsreader.data.network.NYTimesRemoteDataSource
import com.github.nakawai.newsreader.data.network.NYTimesRemoteDataSourceImpl
import com.github.nakawai.newsreader.presentation.articles.ArticlesViewModel
import com.github.nakawai.newsreader.presentation.details.DetailsViewModel
import com.github.nakawai.newsreader.presentation.sections.SectionsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<NYTimesRemoteDataSource> { NYTimesRemoteDataSourceImpl() }
    single<NYTimesLocalDataSource> { NYTimesLocalDataSourceImpl() }
    single<Repository> { RepositoryImpl(get(), get()) }
    viewModel { SectionsViewModel(get()) }
    viewModel { ArticlesViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
}
