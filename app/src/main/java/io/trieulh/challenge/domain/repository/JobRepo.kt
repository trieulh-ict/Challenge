package io.trieulh.challenge.domain.repository

import io.trieulh.challenge.common.DataState
import io.trieulh.challenge.domain.model.Job
import kotlinx.coroutines.flow.Flow

interface JobRepo {
    fun fetchJobInfo(name: String): Flow<DataState<Job>>
    fun updateJobInfo(job: Job): Flow<DataState<Unit>>
}
