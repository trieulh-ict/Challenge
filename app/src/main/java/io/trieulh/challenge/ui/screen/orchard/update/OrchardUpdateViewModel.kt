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
import kotlinx.coroutines.Dispatchers
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

sealed class OrchardUpdateAction {
    object SubmitAction : OrchardUpdateAction()
    class UpdateMaxTreesAction(val name: String) : OrchardUpdateAction()
    class SwitchRateTypeAction(val staff: Staff, val rateType: RateType) : OrchardUpdateAction()
    class ToggleTreeRowAction(val staff: Staff, val rowId: Int) : OrchardUpdateAction()
    class UpdateTreesInRowAction(val staff: Staff, val rowId: Int, val treeNumber: Int) :
        OrchardUpdateAction()

    class RateChangedAction(val staff: Staff, val rate: Int) : OrchardUpdateAction()
    class ApplyRateToAllAction(val subJob: SubJob, val rate: Int) : OrchardUpdateAction()
}

interface OrchardUpdateActionHandler {
    fun handle(action: OrchardUpdateAction)
}

@HiltViewModel
class OrchardUpdateViewModel @Inject constructor(
    private val fetchJobInfoUseCase: FetchJobInfoUseCase,
    private val updateJobInfoUseCase: UpdateJobInfoUseCase,
    private val uiStateManager: OrchardUpdateUiStateManager,
    private val dispatcher: ThreadDispatcher
) : ViewModel(), OrchardUpdateActionHandler {

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

    private fun submitData() {
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

    override fun handle(action: OrchardUpdateAction) {
        viewModelScope.launch(dispatcher.io()) {
            when (action) {
                is OrchardUpdateAction.UpdateMaxTreesAction -> uiStateManager.updateMaxTreesBySubJob(
                    action.name
                )
                is OrchardUpdateAction.SwitchRateTypeAction -> uiStateManager.switchRateType(
                    action.staff,
                    action.rateType
                )
                is OrchardUpdateAction.ToggleTreeRowAction -> uiStateManager.onToggleTreeRow(
                    action.staff,
                    action.rowId
                )
                is OrchardUpdateAction.UpdateTreesInRowAction -> uiStateManager.onUpdateTreesInRow(
                    action.staff,
                    action.rowId,
                    action.treeNumber
                )
                is OrchardUpdateAction.RateChangedAction -> uiStateManager.onRateChanged(
                    action.staff,
                    action.rate
                )
                is OrchardUpdateAction.ApplyRateToAllAction -> uiStateManager.onAppRateToAll(
                    action.subJob,
                    action.rate
                )
                is OrchardUpdateAction.SubmitAction -> submitData()
            }
        }
    }
}
