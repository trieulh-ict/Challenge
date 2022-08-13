package io.trieulh.challenge.ui.screen.orchard.update

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.trieulh.challenge.common.DataState
import io.trieulh.challenge.common.MainDispatcherRule
import io.trieulh.challenge.common.TestThreadDispatcher
import io.trieulh.challenge.data.MockResponse
import io.trieulh.challenge.domain.usecase.FetchJobInfoUseCase
import io.trieulh.challenge.domain.usecase.UpdateJobInfoUseCase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OrchardUpdateViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    private val fetchJobInfoUseCase: FetchJobInfoUseCase = mockk()
    private val updateJobInfoUseCase: UpdateJobInfoUseCase = mockk()
    private val uiStateManager: OrchardUpdateUiStateManager = spyk(OrchardUpdateUiStateManagerImpl(TestThreadDispatcher))

    lateinit var viewModel: OrchardUpdateViewModel

    @Before
    fun setUp() {
        every { fetchJobInfoUseCase(any()) } returns flow { emit(DataState.Success(MockResponse.mockJob)) }
        every { updateJobInfoUseCase(any()) } returns flow { emit(DataState.Success(Unit)) }
        viewModel = OrchardUpdateViewModel(
            fetchJobInfoUseCase,
            updateJobInfoUseCase,
            uiStateManager
        )
    }


    @Test
    fun `Test init success`() = runTest {
        verify(exactly = 1) {
            fetchJobInfoUseCase(any())
            uiStateManager.startFetchingData()
            uiStateManager.updateJob(any())
        }
        uiStateManager.uiState.test {
            var item = awaitItem()
            assertEquals(MockResponse.mockJob.name, item.job.name)
        }
    }

    @Test
    fun `Test submit data success`() = runTest {
        viewModel.navigationEvent.test {
            viewModel.submitData()
            var item = awaitItem()
            assertEquals(OrchardUpdateNavigationState.NavigateToSuccess, item)
            verify(exactly = 1) {
                fetchJobInfoUseCase(any())
                uiStateManager.startSubmittingData()
                uiStateManager.stopSubmittingData()
            }
        }
    }

}