package io.trieulh.challenge.domain.model

data class AvailableRow(
    val completedLogs: List<CompletedLog>,
    val rowId: Int,
    val totalTrees: Int
)
