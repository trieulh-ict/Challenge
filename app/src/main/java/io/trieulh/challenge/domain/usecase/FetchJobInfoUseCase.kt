package io.trieulh.challenge.domain.usecase

import io.trieulh.challenge.common.DataState
import io.trieulh.challenge.domain.model.Job
import io.trieulh.challenge.domain.repository.JobRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchJobInfoUseCase @Inject constructor(val jobRepo: JobRepo) {
    operator fun invoke(name: String): Flow<DataState<Job>> = jobRepo.fetchJobInfo(name)
}