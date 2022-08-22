package io.trieulh.challenge.data.local

import io.trieulh.challenge.domain.model.Job
import javax.inject.Inject

interface JobDataSource {
    suspend fun getJobInfo(name: String): Job?
    suspend fun saveJobInfo(job: Job)
}

class JobDataSourceImpl @Inject constructor() : JobDataSource {
    // Mock a local storage
    private val jobMap = HashMap<String, Job>()

    override suspend fun getJobInfo(name: String): Job? {
        return jobMap.getOrDefault(name, null)
    }

    override suspend fun saveJobInfo(job: Job) {
        jobMap[job.name] = job
    }
}
