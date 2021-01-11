package com.github.nakawai.newsreader.data.db

import androidx.lifecycle.LiveData
import com.github.nakawai.newsreader.data.db.livedata.LiveRealmListData
import com.github.nakawai.newsreader.data.db.realm.HistoryRealmObject
import com.github.nakawai.newsreader.data.db.realm.translate
import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.entities.History
import com.github.nakawai.newsreader.domain.repository.HistoryLocalDataSource
import io.realm.Realm
import io.realm.RealmResults
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class HistoryLocalDataSourceImpl(private val realm: Realm) : HistoryLocalDataSource {


    override suspend fun addHistory(url: ArticleUrl) = suspendCoroutine<Unit> { continuation ->
        realm.executeTransactionAsync { r ->
            r.copyToRealmOrUpdate(HistoryRealmObject().also { it.url = url.value })
            continuation.resume(Unit)
        }
    }

    override fun observeHistories(): LiveData<List<History>> {
        return object : LiveRealmListData<HistoryRealmObject, History>(realm) {
            override fun runQuery(realm: Realm): RealmResults<HistoryRealmObject> {
                return realm.where(HistoryRealmObject::class.java)
                    .findAllAsync()
            }

            override fun translate(realmObject: HistoryRealmObject): History {
                return realmObject.translate()
            }
        }
    }
}
