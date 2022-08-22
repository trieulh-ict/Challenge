package io.trieulh.challenge.domain.model

data class Staff(
    val block: String,
    val orchard: String,
    val assignedRows: List<AssignedRow>,
    val name: String,
    val rate: Int? = null,
    val rateType: RateType
)
