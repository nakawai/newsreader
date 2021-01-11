package com.github.nakawai.newsreader.data.db.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class HistoryRealmObject : RealmObject() {
    @PrimaryKey
    var url: String? = null

    companion object {
        const val URL = "url"
    }
}
