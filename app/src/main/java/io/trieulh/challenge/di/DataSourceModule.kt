package io.trieulh.challenge.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.trieulh.challenge.data.local.JobDataSource
import io.trieulh.challenge.data.local.JobDataSourceImpl
import io.trieulh.challenge.data.remote.JobApi
import io.trieulh.challenge.data.remote.JobApiImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindLocalJobDataSource(source: JobDataSourceImpl): JobDataSource

    @Singleton
    @Binds
    abstract fun bindRemoteJobApi(api: JobApiImpl): JobApi
}
