package com.github.nakawai.newsreader.model.db

import androidx.lifecycle.LiveData
import io.realm.*

/**
 * https://gist.github.com/cmelchior/3fe791f84db37fd3bcb3749d4188168a
 */
abstract class LiveRealmData<T : RealmModel, U : Any>(private val config: RealmConfiguration? = null) : LiveData<List<U>>() {
    private lateinit var results: RealmResults<T>
    private lateinit var realm: Realm
    private val listener = RealmChangeListener<RealmResults<T>> { results ->
        value = results.map { translate(it) }
    }

    override fun onActive() {
        realm = if (config == null) {
            Realm.getDefaultInstance()
        } else {
            Realm.getInstance(config)
        }
        results = runQuery(realm)
        results.addChangeListener(listener)

        value = results.map { translate(it) }
    }

    override fun onInactive() {
        results.removeAllChangeListeners()
        realm.close()
    }

    abstract fun runQuery(realm: Realm): RealmResults<T>

    abstract fun translate(original: T): U
}
