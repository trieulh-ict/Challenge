package io.trieulh.challenge.utils

import kotlinx.coroutines.CoroutineDispatcher

interface ThreadDispatcher {
    fun io(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
    fun main(): CoroutineDispatcher
    fun unconfined(): CoroutineDispatcher
}
