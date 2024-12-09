package com.hyperkani.gifs_app.hilt

import com.hyperkani.gifs_app.data.remote.GifsApi
import com.hyperkani.gifs_app.data.repository.GifsRepository
import com.hyperkani.gifs_app.data.repository.GifsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(gifsApi: GifsApi): GifsRepository =
        GifsRepositoryImpl(gifsApi)
}