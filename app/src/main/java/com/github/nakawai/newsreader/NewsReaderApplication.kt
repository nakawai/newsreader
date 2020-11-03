package com.github.nakawai.newsreader

import android.app.Application
import com.facebook.stetho.Stetho
import io.realm.Realm
import io.realm.RealmConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

@Suppress("unused")
class NewsReaderApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Configure default configuration for Realm
        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
            .build()
        Realm.setDefaultConfiguration(realmConfig)

        // Stetho
        Stetho.initializeWithDefaults(this)

        // Timber
        Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@NewsReaderApplication)
            modules(appModule)
        }

    }
}
