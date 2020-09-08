package com.github.nakawai.newsreader

import android.app.Application
import android.util.Log
import com.facebook.stetho.Stetho
import com.google.firebase.iid.FirebaseInstanceId
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

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@addOnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                Log.i(TAG, "token:$token")

            }

    }

    companion object {
        const val TAG = "NewsReaderApplication"
    }
}
