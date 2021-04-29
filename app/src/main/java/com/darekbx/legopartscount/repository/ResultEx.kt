package com.darekbx.legopartscount.repository

sealed class ResultEx<out T : Any> {
    object Loading : ResultEx<Nothing>()
    class Value<out T : Any>(@JvmField val value: T) : ResultEx<T>()
    class Error(@JvmField val error: Throwable) : ResultEx<Nothing>()
}
