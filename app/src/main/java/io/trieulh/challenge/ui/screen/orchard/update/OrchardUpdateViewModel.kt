package io.trieulh.challenge.ui.screen.orchard.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.trieulh.challenge.common.onFailed
import io.trieulh.challenge.common.onLoading
import io.trieulh.challenge.common.onSuccess
import io.trieulh.challenge.data.MockResponse
import io.trieulh.challenge.domain.model.Job
import io.trieulh.challenge.domain.model.RateType
import io.trieulh.challenge.domain.model.Staff
import io.trieulh.challenge.domain.model.SubJob
import io.trieulh.challenge.domain.usecase.FetchJobInfoUseCase
import io.trieulh.challenge.domain.usecase.UpdateJobInfoUseCase
import io.trieulh.challenge.utils.ThreadDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


data class OrchardUpdateUiState(
    val isFetchingData: Boolean = true,
    val isSubmittingData: Boolean = false,
    val job: Job = Job()
)

sealed class OrchardUpdateNavigationState {
    object NavigateToSuccess : OrchardUpdateNavigationState()
}

sealed class OrchardUpdateUiStateAction {
    class UpdateMaxTreesAction(val name: String) : OrchardUpdateUiStateAction()
    class SwitchRateTypeAction(val staff: Staff, val rateType: RateType) :
        OrchardUpdateUiStateAction()

    class ToggleTreeRowAction(val staff: Staff, val rowId: Int) : OrchardUpdateUiStateAction()
    class UpdateTreesInRowAction(val staff: Staff, val rowId: Int, val treeNumber: Int) :
        OrchardUpdateUiStateAction()

    class RateChangedAction(val staff: Staff, val rate: Int) : OrchardUpdateUiStateAction()
    class ApplyRateToAllAction(val subJob: SubJob, val rate: Int) : OrchardUpdateUiStateAction()
}

interface OrchardUpdateUiStateHandler {
    fun handle(action: OrchardUpdateUiStateAction)
}

@HiltViewModel
class OrchardUpdateViewModel @Inject constructor(
    private val fetchJobInfoUseCase: FetchJobInfoUseCase,
    private val updateJobInfoUseCase: UpdateJobInfoUseCase,
    private val uiStateManager: OrchardUpdateUiStateManager,
    private val dispatcher: ThreadDispatcher
) : ViewModel(), OrchardUpdateUiStateHandler {

    val uiState: StateFlow<OrchardUpdateUiState> = uiStateManager.uiState

    private val _navigationEvent = MutableSharedFlow<OrchardUpdateNavigationState?>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        fetchJobInfoUseCase(MockResponse.mockJob.name)
            .onStart { uiStateManager.startFetchingData() }
            .onEach { state ->
                state.onSuccess {
                    uiStateManager.updateJob(it)
                }
                state.onLoading {
                    it?.let {
                        uiStateManager.updateJob(it)
                    }
                }
                state.onFailed {
                    uiStateManager.stopFetchingData()
                }
            }
            .launchIn(viewModelScope)
    }

    fun submitData() {
        updateJobInfoUseCase(uiState.value.job)
            .onStart {
                uiStateManager.startSubmittingData()
            }
            .onEach { state ->
                state.onSuccess {
                    navigateToSuccess()
                }
            }
            .onCompletion {
                uiStateManager.stopSubmittingData()
            }
            .launchIn(viewModelScope)
    }

    private fun navigateToSuccess() {
        viewModelScope.launch {
            _navigationEvent.emit(OrchardUpdateNavigationState.NavigateToSuccess)
        }
    }

    override fun handle(action: OrchardUpdateUiStateAction) {
        viewModelScope.launch(dispatcher.default()) {
            when (action) {
                is OrchardUpdateUiStateAction.UpdateMaxTreesAction -> uiStateManager.updateMaxTreesBySubJob(
                    action.name
                )
                is OrchardUpdateUiStateAction.SwitchRateTypeAction -> uiStateManager.switchRateType(
                    action.staff,
                    action.rateType
                )
                is OrchardUpdateUiStateAction.ToggleTreeRowAction -> uiStateManager.onToggleTreeRow(
                    action.staff,
                    action.rowId
                )
                is OrchardUpdateUiStateAction.UpdateTreesInRowAction -> uiStateManager.onUpdateTreesInRow(
                    action.staff,
                    action.rowId,
                    action.treeNumber
                )
                is OrchardUpdateUiStateAction.RateChangedAction -> uiStateManager.onRateChanged(
                    action.staff,
                    action.rate
                )
                is OrchardUpdateUiStateAction.ApplyRateToAllAction -> uiStateManager.onAppRateToAll(
                    action.subJob,
                    action.rate
                )
            }
        }
    }
}
