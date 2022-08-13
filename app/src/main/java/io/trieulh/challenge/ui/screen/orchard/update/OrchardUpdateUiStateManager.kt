package io.trieulh.challenge.ui.screen.orchard.update

import io.trieulh.challenge.domain.model.*
import io.trieulh.challenge.utils.ThreadDispatcher
import io.trieulh.challenge.utils.update
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.lang.Integer.min
import javax.inject.Inject

interface OrchardUpdateUiStateManager {
    val uiState: StateFlow<OrchardUpdateUiState>

    fun stopFetchingData()
    fun startFetchingData()
    fun startSubmittingData()
    fun stopSubmittingData()
    fun updateJob(job: Job)
    fun updateMaxTreesBySubJob(name: String)
    fun switchRateType(staff: Staff, rateType: RateType)
    fun onToggleTreeRow(staff: Staff, rowId: Int)
    fun onUpdateTreesInRow(staff: Staff, rowId: Int, treeNumber: Int)
    fun onRateChanged(staff: Staff, rate: Int)
    fun onAppRateToAll(subJob: SubJob, rate: Int)
}

class OrchardUpdateUiStateManagerImpl @Inject constructor(threadDispatcher: ThreadDispatcher) : OrchardUpdateUiStateManager {
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

    override fun startFetchingData() {
        _uiState.update { copy(isFetchingData = true) }
    }

    override fun startSubmittingData() {
        _uiState.update { copy(isSubmittingData = true) }
    }

    override fun stopSubmittingData() {
        _uiState.update { copy(isSubmittingData = false) }
    }

    override fun stopFetchingData() {
        _uiState.update { copy(isFetchingData = false) }
    }

    override fun updateJob(job: Job) {
        _uiState.update { copy(job = job, isFetchingData = false) }
    }

    override fun updateMaxTreesBySubJob(name: String) {
        uiState.value.job.subJobs.firstOrNull() { it.name == name }?.let { subJob ->
            val availableMaxTreeForRows = subJob.availableRows.associate { availableRow ->
                val assignedStaffs = subJob.staffs.filter { it.assignedRows.any { it.rowId == availableRow.rowId } }
                val availableMaxTreeForEach = if (assignedStaffs.isNotEmpty()) {
                    (availableRow.totalTrees - availableRow.completedLogs.sumOf { it.completed }) / assignedStaffs.count()
                } else null
                availableRow.rowId to availableMaxTreeForEach
            }
            subJob.copy(
                staffs = subJob.staffs.map { staff ->
                    staff.copy(
                        assignedRows = staff.assignedRows.map { assignedRow ->
                            assignedRow.copy(assignedTrees = availableMaxTreeForRows.get(assignedRow.rowId) ?: assignedRow.assignedTrees)
                        }
                    )
                }
            )
        }?.also { subJob ->
            replaceSubJob(subJob)
        }
    }

    override fun switchRateType(staff: Staff, rateType: RateType) {
        uiState.value.job.subJobs.firstOrNull() { it.staffs.any { it.name == staff.name } }?.let { subJob ->
            subJob.copy(
                staffs = subJob.staffs.map {
                    if (it.name != staff.name) {
                        it
                    } else {
                        it.copy(rateType = rateType)
                    }
                }
            )
        }?.also { subJob ->
            replaceSubJob(subJob)
        }
    }

    override fun onToggleTreeRow(staff: Staff, rowId: Int) {
        uiState.value.job.subJobs.firstOrNull() { it.staffs.any { it.name == staff.name } }?.let { subJob ->
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
        }?.also { subJob ->
            replaceSubJob(subJob)
        }
    }

    override fun onUpdateTreesInRow(staff: Staff, rowId: Int, treeNumber: Int) {
        uiState.value.job.subJobs.firstOrNull() { it.staffs.any { it.name == staff.name } }?.let { subJob ->
            val availableTreeNumber = min(treeNumber, subJob.availableRows.firstOrNull { it.rowId == rowId }?.let {
                it.totalTrees - it.completedLogs.sumOf { it.completed }
            } ?: Int.MAX_VALUE)
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
        }?.also { subJob ->
            replaceSubJob(subJob)
        }
    }

    override fun onRateChanged(staff: Staff, rate: Int) {
        uiState.value.job.subJobs.firstOrNull() { it.staffs.any { it.name == staff.name } }?.let { subJob ->
            subJob.copy(
                staffs = subJob.staffs.map {
                    if (it.name != staff.name) {
                        it
                    } else {
                        it.copy(rate = rate)
                    }
                }
            )
        }?.also { subJob ->
            replaceSubJob(subJob)
        }
    }

    override fun onAppRateToAll(appliedSubJob: SubJob, rate: Int) {
        uiState.value.job.subJobs.firstOrNull() { it.name == appliedSubJob.name }?.let { subJob ->
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