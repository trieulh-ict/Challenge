package io.trieulh.challenge.common

sealed class DataState<out T> {
    class Success<out T>(val data: T) : DataState<T>()
    class Loading<out T>(val data: T? = null) : DataState<T>()
    class Failed<out T>(val error: Error) : DataState<T>()
}

suspend fun <T> DataState<T>.onSuccess(action: suspend (T) -> Unit) {
    if (this is DataState.Success) action(this.data)
}

suspend fun <T> DataState<T>.onLoading(action: suspend (T?) -> Unit) {
    if (this is DataState.Loading) action(this.data)
}

suspend fun <T> DataState<T>.onFailed(action: suspend (Error) -> Unit) {
    if (this is DataState.Failed) action(this.error)
}
