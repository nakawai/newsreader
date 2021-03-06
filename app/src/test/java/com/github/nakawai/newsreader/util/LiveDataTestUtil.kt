package com.github.nakawai.newsreader.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * GoogleのサンプルコードからもってきたLiveDataのテスト用ktx
 *
 * TODO 最新のテスト実装に追従する https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/
 */
fun <T> LiveData<T>.observeForTesting(block: () -> Unit) {
    val observer = Observer<T> { block() }
    try {
        observeForever(observer)
    } finally {
        removeObserver(observer)
    }
}
