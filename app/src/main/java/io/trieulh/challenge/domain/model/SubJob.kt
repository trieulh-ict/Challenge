package io.trieulh.challenge.domain.model

data class SubJob(
    val name: String,
    val availableRows: List<AvailableRow>,
    val staffs: List<Staff>
)
