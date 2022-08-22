package io.trieulh.challenge.data.repository

import io.trieulh.challenge.common.DataState
import io.trieulh.challenge.data.local.JobDataSource
import io.trieulh.challenge.data.remote.JobApi
import io.trieulh.challenge.di.IODispatcher
import io.trieulh.challenge.domain.model.Job
import io.trieulh.challenge.domain.repository.JobRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Error
import javax.inject.Inject

class JobRepoImpl @Inject constructor(
    private val jobLocalSource: JobDataSource,
    private val jobApi: JobApi,
    @IODispatcher val ioDispatcher: CoroutineDispatcher
) : JobRepo {
    override fun fetchJobInfo(name: String): Flow<DataState<Job>> = flow {
        emit(DataState.Loading(jobLocalSource.getJobInfo(name)))
        val result = jobApi.fetchJobInfo(name)
        emit(
            when {
                result.isSuccess -> result.getOrNull()?.let {
                    DataState.Success(it)
                } ?: run {
                    DataState.Failed<Job>(Error("Data is Empty."))
                }
                else -> DataState.Failed<Job>(Error("Something wrong. Please Try again"))
            }
        )
    }.flowOn(ioDispatcher)

    override fun updateJobInfo(job: Job): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading())
        jobLocalSource.saveJobInfo(job)
        val result = jobApi.saveJobInfo(job)
        emit(
            when {
                result.isSuccess -> DataState.Success(Unit)
                else -> DataState.Failed(Error("Something wrong. Please Try again"))
            }
        )
    }.flowOn(ioDispatcher)
}
