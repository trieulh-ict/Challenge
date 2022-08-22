package io.trieulh.challenge.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.trieulh.challenge.utils.ThreadDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class IODispatcher

@Module
@InstallIn(SingletonComponent::class)
class ConfigModule {

    @Provides
    @Singleton
    @IODispatcher
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideThreadDispatcher(): ThreadDispatcher = object : ThreadDispatcher {
        override fun io(): CoroutineDispatcher = Dispatchers.IO
        override fun default(): CoroutineDispatcher = Dispatchers.Default
        override fun main(): CoroutineDispatcher = Dispatchers.Main
        override fun unconfined(): CoroutineDispatcher = Dispatchers.Unconfined
    }
}
