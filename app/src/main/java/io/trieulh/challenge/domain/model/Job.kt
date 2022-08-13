package io.trieulh.challenge.domain.model

data class Job(
    val name: String = "",
    val subJobs: List<SubJob> = listOf()
)
