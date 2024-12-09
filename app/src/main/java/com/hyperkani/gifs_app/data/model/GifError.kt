package com.hyperkani.gifs_app.data.model

sealed class GifError : Exception() {
    data object NetworkError : GifError()
    data object ServerError : GifError()
    data object UnknownError : GifError()
    data class ApiError(val code: Int, override val message: String) : GifError()
}