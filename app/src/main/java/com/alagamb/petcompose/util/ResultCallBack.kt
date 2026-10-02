package com.alagamb.petcompose.util

import androidx.annotation.StringRes
import com.alagamb.petcompose.R
import java.io.IOException

enum class ErrorType {
    NETWORK,
    SERVER,
}

sealed class ResultCallBack<out T : Any> {
    data class Success<out T : Any>(val data: T) : ResultCallBack<T>()
    data class Error(
        val type: ErrorType,
        val message: String? = null,
        val statusCode: Int? = null,
        @get:StringRes val messageRes: Int? = null,
        val throwable: Throwable? = null
    ) : ResultCallBack<Nothing>()

    // ──  Enhancements for clean handling  ─────────────────────────
    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error

    fun getOrNull(): T? = (this as? Success)?.data

    inline fun onSuccess(action: (data: T) -> Unit): ResultCallBack<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onError(action: (error: Error) -> Unit): ResultCallBack<T> {
        if (this is Error) action(this)
        return this
    }

    inline fun <R : Any> map(transform: (T) -> R): ResultCallBack<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> this
        }
    }
}

fun Exception.toFailure(): ResultCallBack.Error {
    return when (this) {
        is IOException -> ResultCallBack.Error(
            type = ErrorType.NETWORK,
            message = "Check your internet connection.",
            messageRes = R.string.error_network,
            throwable = this
        )
        else -> ResultCallBack.Error(
            type = ErrorType.SERVER,
            message = localizedMessage ?: "Something went wrong. Please try again later.",
            messageRes = R.string.error_unknown,
            throwable = this
        )
    }
}
