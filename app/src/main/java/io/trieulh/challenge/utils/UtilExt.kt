package io.trieulh.challenge.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.updateAndGet

fun <T> MutableStateFlow<T>.update(onUpdated: ((T) -> Unit)? = null, transform: T.() -> T) {
    val updatedState = updateAndGet { it.transform() }
    onUpdated?.invoke(updatedState)
}