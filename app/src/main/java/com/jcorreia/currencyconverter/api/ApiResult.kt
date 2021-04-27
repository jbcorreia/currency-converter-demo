package com.jcorreia.currencyconverter.api

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class UnknownError(val errorMessage: String) : ApiResult<Nothing>()
}
