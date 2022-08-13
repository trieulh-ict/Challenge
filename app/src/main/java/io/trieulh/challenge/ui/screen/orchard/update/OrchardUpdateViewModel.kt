package io.trieulh.challenge.ui.screen.orchard.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.trieulh.challenge.common.onFailed
import io.trieulh.challenge.common.onLoading
import io.trieulh.challenge.common.onSuccess
import io.trieulh.challenge.data.MockResponse
import io.trieulh.challenge.domain.model.Job
import io.trieulh.challenge.domain.usecase.FetchJobInfoUseCase
import io.trieulh.challenge.domain.usecase.UpdateJobInfoUseCase
import io.trieulh.challenge.utils.update
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

@HiltViewModel
class OrchardUpdateViewModel @Inject constructor(
    private val fetchJobInfoUseCase: FetchJobInfoUseCase,
    private val updateJobInfoUseCase: UpdateJobInfoUseCase,
    private val uiStateManager: OrchardUpdateUiStateManager,
) : ViewModel(), OrchardUpdateUiStateManager by uiStateManager {

    private val _navigationEvent = MutableSharedFlow<OrchardUpdateNavigationState?>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            uiStateManager.startFetchingData()
            // Because This is starting screen so I will mock the arg.
            fetchJobInfoUseCase(MockResponse.mockJob.name)
                .collect { state ->
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
        }
    }

    fun submitData() {
        viewModelScope.launch {
            uiStateManager.startSubmittingData()
            updateJobInfoUseCase(uiState.value.job)
                .collectLatest { state ->
                    state.onSuccess {
                        uiStateManager.stopSubmittingData()
                        navigateToSuccess()
                    }
                }
        }
    }

    private fun navigateToSuccess() {
        viewModelScope.launch {
            _navigationEvent.emit(OrchardUpdateNavigationState.NavigateToSuccess)
        }
    }
}
