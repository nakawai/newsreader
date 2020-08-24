package com.github.nakawai.newsreader

import android.app.Application
import com.facebook.stetho.Stetho
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber

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

    }
}
