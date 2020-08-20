/*
 * Copyright 2016 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.nakawai.newsreader

import android.app.Application
import android.content.Context
import io.reactivex.plugins.RxJavaPlugins
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber

class NewsReaderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
        Timber.plant(Timber.DebugTree())
        RxJavaPlugins.setErrorHandler { throwable: Throwable ->
            Timber.e(throwable.toString())
        }

        // Configure default configuration for Realm
        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder().build()
        Realm.setDefaultConfiguration(realmConfig)
    }

    companion object {
        lateinit var context: Context
            private set
    }
}