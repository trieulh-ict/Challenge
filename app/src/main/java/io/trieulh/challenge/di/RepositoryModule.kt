package io.trieulh.challenge.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.trieulh.challenge.data.repository.JobRepoImpl
import io.trieulh.challenge.domain.repository.JobRepo
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindJobRepo(jobRepoImpl: JobRepoImpl): JobRepo
}