package com.github.nakawai.newsreader.di

import android.content.Context
import androidx.room.Room
import com.github.nakawai.newsreader.NewsReaderApplication
import com.github.nakawai.newsreader.data.db.ArticleLocalDataSourceImpl
import com.github.nakawai.newsreader.data.db.ConfigLocalDataSourceImpl
import com.github.nakawai.newsreader.data.db.HistoryLocalDataSourceImpl
import com.github.nakawai.newsreader.data.db.room.AppDatabase
import com.github.nakawai.newsreader.data.network.ArticleRemoteDataSourceImpl
import com.github.nakawai.newsreader.domain.datasource.ArticleLocalDataSource
import com.github.nakawai.newsreader.domain.repository.ArticleRepository
import com.github.nakawai.newsreader.domain.repository.ArticleRepositoryImpl
import com.github.nakawai.newsreader.domain.repository.HistoryLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.realm.Realm
import javax.inject.Singleton

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
    @ApplicationContext
    fun provideApplicationContext(app: NewsReaderApplication): Context {
        return app.applicationContext
    }

    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "newsreader.db").build()
    }

    @Provides
    @Singleton
    fun provideHistoryRepository(db: AppDatabase): HistoryLocalDataSource {
        return HistoryLocalDataSourceImpl(db)
    }

    @Provides
    fun provideRealm(): Realm {
        return Realm.getDefaultInstance()
    }

}
