package com.github.nakawai.newsreader.di

import com.github.nakawai.newsreader.data.db.ArticleLocalDataSourceImpl
import com.github.nakawai.newsreader.data.db.ConfigLocalDataSourceImpl
import com.github.nakawai.newsreader.data.db.HistoryLocalDataSourceImpl
import com.github.nakawai.newsreader.data.network.ArticleRemoteDataSourceImpl
import com.github.nakawai.newsreader.domain.datasource.ArticleLocalDataSource
import com.github.nakawai.newsreader.domain.repository.ArticleRepository
import com.github.nakawai.newsreader.domain.repository.ArticleRepositoryImpl
import com.github.nakawai.newsreader.domain.repository.HistoryLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import io.realm.Realm

@Module
@InstallIn(ActivityComponent::class)
object NewsReaderDaggerModule {

    @Provides
    fun provideArticleRepository(articleLocalDataSource: ArticleLocalDataSource): ArticleRepository {
        return ArticleRepositoryImpl(
            articleLocalDataSource,
            ArticleRemoteDataSourceImpl(),
            ConfigLocalDataSourceImpl()
        )
    }

    @Provides
    fun provideArticleLocalDataSource(realm: Realm): ArticleLocalDataSource {
        return ArticleLocalDataSourceImpl(realm)
    }

    @Provides
    fun provideHistoryRepository(realm: Realm): HistoryLocalDataSource {
        return HistoryLocalDataSourceImpl(realm)
    }

    @Provides
    fun provideRealm(): Realm {
        return Realm.getDefaultInstance()
    }

}
