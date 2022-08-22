package io.trieulh.challenge.domain.usecase

import io.trieulh.challenge.domain.model.Job
import io.trieulh.challenge.domain.repository.JobRepo
import javax.inject.Inject

class UpdateJobInfoUseCase @Inject constructor(private val jobRepo: JobRepo) {
    operator fun invoke(job: Job) = jobRepo.updateJobInfo(job)
}
