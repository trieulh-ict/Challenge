package io.trieulh.challenge.ui.screen.orchard.update

import io.trieulh.challenge.domain.model.*
import io.trieulh.challenge.utils.ThreadDispatcher
import io.trieulh.challenge.utils.update
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.lang.Integer.min
import javax.inject.Inject

interface OrchardUpdateUiStateManager {
    val uiState: StateFlow<OrchardUpdateUiState>

    suspend fun stopFetchingData()
    suspend fun startFetchingData()
    suspend fun startSubmittingData()
    suspend fun stopSubmittingData()
    suspend fun updateJob(job: Job)
    suspend fun updateMaxTreesBySubJob(name: String)
    suspend fun switchRateType(staff: Staff, rateType: RateType)
    suspend fun onToggleTreeRow(staff: Staff, rowId: Int)
    suspend fun onUpdateTreesInRow(staff: Staff, rowId: Int, treeNumber: Int)
    suspend fun onRateChanged(staff: Staff, rate: Int)
    suspend fun onAppRateToAll(subJob: SubJob, rate: Int)
}

class OrchardUpdateUiStateManagerImpl @Inject constructor(private val dispatcher: ThreadDispatcher) :
    OrchardUpdateUiStateManager {
    private val _uiState = MutableStateFlow(OrchardUpdateUiState())
    override val uiState = _uiState.asStateFlow()

    private fun replaceSubJob(subJob: SubJob) {
        _uiState.update {
            copy(
                job = job.copy(
                    subJobs = job.subJobs.map { if (it.name == subJob.name) subJob else it }
                )
            )
        }
    }

    override suspend fun startFetchingData() = withContext(dispatcher.default()) {
        _uiState.update { copy(isFetchingData = true) }
    }

    override suspend fun startSubmittingData() = withContext(dispatcher.default()) {
        _uiState.update { copy(isSubmittingData = true) }
    }

    override suspend fun stopSubmittingData() = withContext(dispatcher.default()) {
        _uiState.update { copy(isSubmittingData = false) }
    }

    override suspend fun stopFetchingData() = withContext(dispatcher.default()) {
        _uiState.update { copy(isFetchingData = false) }
    }

    override suspend fun updateJob(job: Job) = withContext(dispatcher.default()) {
        _uiState.update { copy(job = job, isFetchingData = false) }
    }

    override suspend fun updateMaxTreesBySubJob(name: String): Unit =
        withContext(dispatcher.default()) {
            uiState.value.job.subJobs.firstOrNull() { it.name == name }
                ?.let { subJob ->
                    val availableMaxTreeForRows = subJob.availableRows.associate { availableRow ->
                        val assignedStaffs =
                            subJob.staffs.filter { it.assignedRows.any { it.rowId == availableRow.rowId } }
                        val availableMaxTreeForEach = if (assignedStaffs.isNotEmpty()) {
                            (availableRow.totalTrees - availableRow.completedLogs.sumOf { it.completed }) / assignedStaffs.count()
                        } else null
                        availableRow.rowId to availableMaxTreeForEach
                    }
                    subJob.copy(
                        staffs = subJob.staffs.map { staff ->
                            staff.copy(
                                assignedRows = staff.assignedRows.map { assignedRow ->
                                    assignedRow.copy(
                                        assignedTrees = availableMaxTreeForRows.get(assignedRow.rowId)
                                            ?: assignedRow.assignedTrees
                                    )
                                }
                            )
                        }
                    )
                }
                ?.let { subJob ->
                    replaceSubJob(subJob)
                }
        }

    override suspend fun switchRateType(staff: Staff, rateType: RateType): Unit =
        withContext(dispatcher.default()) {
            uiState.value.job.subJobs.firstOrNull() { it.staffs.any { it.name == staff.name } }
                ?.let { subJob ->
                    subJob.copy(
                        staffs = subJob.staffs.map {
                            if (it.name != staff.name) {
                                it
                            } else {
                                it.copy(rateType = rateType)
                            }
                        }
                    )
                }
                ?.let { subJob ->
                    replaceSubJob(subJob)
                }
        }

    override suspend fun onToggleTreeRow(staff: Staff, rowId: Int): Unit =
        withContext(dispatcher.default()) {
            uiState.value.job.subJobs.firstOrNull() { it.staffs.any { it.name == staff.name } }
                ?.let { subJob ->
                    subJob.copy(
                        staffs = subJob.staffs.map {
                            if (it.name != staff.name) {
                                it
                            } else {
                                it.copy(
                                    assignedRows = if (it.assignedRows.any { it.rowId == rowId }) {
                                        it.assignedRows.filter { it.rowId != rowId }
                                    } else {
                                        it.assignedRows.toMutableList().apply {
                                            add(AssignedRow(0, rowId))
                                        }.sortedBy { it.rowId }
                                    }
                                )
                            }
                        }
                    )
                }
                ?.let { subJob ->
                    replaceSubJob(subJob)
                }
        }

    override suspend fun onUpdateTreesInRow(staff: Staff, rowId: Int, treeNumber: Int): Unit =
        withContext(dispatcher.default()) {
            uiState.value.job.subJobs.firstOrNull() { it.staffs.any { it.name == staff.name } }
                ?.let { subJob ->
                    val availableTreeNumber = min(
                        treeNumber,
                        subJob.availableRows.firstOrNull { it.rowId == rowId }?.let {
                            it.totalTrees - it.completedLogs.sumOf { it.completed }
                        } ?: Int.MAX_VALUE
                    )
                    subJob.copy(
                        staffs = subJob.staffs.map {
                            if (it.name != staff.name) {
                                it
                            } else {
                                it.copy(
                                    assignedRows = if (it.assignedRows.any { it.rowId == rowId }) {
                                        it.assignedRows.map {
                                            it.copy(
                                                assignedTrees = when (it.rowId) {
                                                    rowId -> availableTreeNumber
                                                    else -> it.assignedTrees
                                                }
                                            )
                                        }
                                    } else {
                                        it.assignedRows
                                    }
                                )
                            }
                        }
                    )
                }
                ?.let { subJob ->
                    replaceSubJob(subJob)
                }
        }

    override suspend fun onRateChanged(staff: Staff, rate: Int): Unit =
        withContext(dispatcher.default()) {
            uiState.value.job.subJobs.firstOrNull() { it.staffs.any { it.name == staff.name } }
                ?.let { subJob ->
                    subJob.copy(
                        staffs = subJob.staffs.map {
                            if (it.name != staff.name) {
                                it
                            } else {
                                it.copy(rate = rate)
                            }
                        }
                    )
                }
                ?.let { subJob ->
                    replaceSubJob(subJob)
                }
        }

    override suspend fun onAppRateToAll(appliedSubJob: SubJob, rate: Int): Unit =
        withContext(dispatcher.default()) {
            uiState.value.job.subJobs.firstOrNull() { it.name == appliedSubJob.name }
                ?.let { subJob ->
                    subJob.copy(
                        staffs = subJob.staffs.map {
                            it.copy(rate = rate)
                        }
                    )
                }?.also { subJob ->
                    replaceSubJob(subJob)
                }
        }
}
