package com.hyperkani.gifs_app.data.repository

import com.hyperkani.gifs_app.data.model.Gif
import kotlinx.coroutines.flow.Flow

interface GifsRepository {

    fun getGifs(): Flow<Result<List<Gif>>>
}