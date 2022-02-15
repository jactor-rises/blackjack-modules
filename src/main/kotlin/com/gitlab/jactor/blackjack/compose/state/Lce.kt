package com.gitlab.jactor.blackjack.compose.state

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class Lce<out T>(
    private val runScope: MainCoroutineDispatcher = Dispatchers.Main,
    private val ioScope: CoroutineDispatcher = Dispatchers.IO
) {
    object Loading : Lce<Nothing>()
    data class Error(val error: Throwable) : Lce<Nothing>()
    data class Content<T>(val data: T) : Lce<T>()

    protected fun <T> invoke(run: () -> T, lceConsumer: (Lce<T>) -> Unit) {
        lceConsumer.invoke(Loading as Lce<T>)

        CoroutineScope(ioScope).launch {
            val content = runForContent(run, lceConsumer)

            if (content != null) {
                withContext(runScope) {
                    lceConsumer.invoke(Content(content))
                }
            }
        }
    }

    private suspend fun <T> runForContent(run: () -> T, lceConsumer: (Lce<T>) -> Unit): T? = try {
        run.invoke()
    } catch (throwable: Throwable) {
        withContext(runScope) {
            lceConsumer.invoke(Error(throwable))
        }

        null
    }
}
