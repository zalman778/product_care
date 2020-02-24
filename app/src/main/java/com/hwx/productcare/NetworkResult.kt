package com.hwx.productcare


sealed class NetworkResult<out T : Any?>

class NetworkSuccess<out T : Any?>(val data: T?) : NetworkResult<T>()

class NetworkError(
    val exception: Throwable,
    val message: String = exception.message.orEmpty() //?: ErrorHandler.UNKNOWN_ERROR
) : NetworkResult<Nothing>()

class NetworkProgress(val isLoading: Boolean) : NetworkResult<Nothing>()