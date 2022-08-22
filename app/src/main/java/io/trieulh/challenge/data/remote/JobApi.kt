package io.trieulh.challenge.data.remote

import io.trieulh.challenge.data.MockResponse
import io.trieulh.challenge.domain.model.Job
import kotlinx.coroutines.delay
import javax.inject.Inject

interface JobApi {
    suspend fun fetchJobInfo(name: String): Result<Job>
    suspend fun saveJobInfo(job: Job): Result<Unit>
}

// Mock a Remote Api
class JobApiImpl @Inject constructor() : JobApi {
    // Mock a remote storage
    private val jobMap = HashMap<String, Job>()

    override suspend fun fetchJobInfo(name: String): Result<Job> {
        // Mock waiting time
        delay(1500L)
        return Result.success(jobMap.getOrPut(name) { MockResponse.mockJob })
    }

    override suspend fun saveJobInfo(job: Job): Result<Unit> {
        // Mock waiting time
        delay(1500L)
        jobMap[job.name] = job
        return Result.success(Unit)
    }
}
