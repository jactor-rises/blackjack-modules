package com.gitlab.jactor.blackjack.compose.state

/**
 * A class representing LCE paradigm (Loading, Content, Error)
 */
sealed class Lce<out T> {
    object Loading : Lce<Nothing>()
    data class Content<T>(val data: T?) : Lce<T?>()
    data class Error(val error: Throwable) : Lce<Nothing>()
}
