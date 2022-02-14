package com.gitlab.jactor.blackjack.compose.state

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class Lce<out T>(
    private val runScope: MainCoroutineDispatcher = Dispatchers.Main,
    private val ioScope: CoroutineDispatcher = Dispatchers.IO,
) {
    object Loading : Lce<Nothing>() {
        var loadingContet = false
    }

    data class Error(val error: Throwable) : Lce<Nothing>()
    data class Content<T>(val data: T) : Lce<T>() {
        init {
            Loading.loadingContet = false
        }
    }


    protected fun <T> invoke(invoke: () -> T, contentConsumer: (Lce<T>) -> Unit) {
        CoroutineScope(ioScope).launch {
            Loading.loadingContet = true
            val content = invoke.invoke()

            withContext(runScope) {
                contentConsumer.invoke(Content(content))
                Loading.loadingContet = false
            }
        }
    }
}
