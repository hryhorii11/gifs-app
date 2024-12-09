package com.hyperkani.gifs_app.data.repository

import android.util.Log
import com.hyperkani.gifs_app.data.model.Gif
import com.hyperkani.gifs_app.data.model.GifError
import com.hyperkani.gifs_app.data.remote.GifsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class GifsRepositoryImpl @Inject constructor(
    private val gifsApi: GifsApi
) : GifsRepository {
    override fun getGifs(): Flow<Result<List<Gif>>> {
        return flow {
            emit(Result.success(gifsApi.getGifs().data))
        }.flowOn(Dispatchers.IO).catch {
            Log.i("tag", it.toString())
            emit(Result.failure(handleNetworkException(it)))
        }
    }

    private fun handleNetworkException(exception: Throwable): GifError {
        return when (exception) {
            is IOException -> GifError.NetworkError
            is HttpException -> {
                when (exception.code()) {
                    in 400..499 -> GifError.ApiError(exception.code(), exception.message())
                    in 500..599 -> GifError.ServerError
                    else -> GifError.UnknownError
                }
            }

            else -> GifError.UnknownError
        }
    }

}