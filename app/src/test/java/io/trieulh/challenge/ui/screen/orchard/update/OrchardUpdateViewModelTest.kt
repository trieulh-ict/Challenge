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
import io.trieulh.challenge.domain.model.RateType
import io.trieulh.challenge.domain.usecase.FetchJobInfoUseCase
import io.trieulh.challenge.domain.usecase.UpdateJobInfoUseCase
import io.trieulh.challenge.utils.ThreadDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OrchardUpdateViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    private val fetchJobInfoUseCase: FetchJobInfoUseCase = mockk()
    private val updateJobInfoUseCase: UpdateJobInfoUseCase = mockk()
    private val testDispatcher: ThreadDispatcher = TestThreadDispatcher
    private val uiStateManager: OrchardUpdateUiStateManager =
        spyk(OrchardUpdateUiStateManagerImpl(TestThreadDispatcher))

    lateinit var viewModel: OrchardUpdateViewModel

    @Before
    fun setUp() {
        every { fetchJobInfoUseCase(any()) } returns flow { emit(DataState.Success(MockResponse.mockJob)) }
        every { updateJobInfoUseCase(any()) } returns flow { emit(DataState.Success(Unit)) }
        viewModel = OrchardUpdateViewModel(
            fetchJobInfoUseCase,
            updateJobInfoUseCase,
            uiStateManager,
            testDispatcher
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
            viewModel.handle(OrchardUpdateAction.SubmitAction)
            var item = awaitItem()
            assertEquals(OrchardUpdateNavigationState.NavigateToSuccess, item)
            verify(exactly = 1) {
                fetchJobInfoUseCase(any())
                uiStateManager.startSubmittingData()
                uiStateManager.stopSubmittingData()
            }
        }
    }

    @Test
    fun `Test handle UpdateMaxTreesAction`() = runTest {
        viewModel.handle(OrchardUpdateAction.UpdateMaxTreesAction(MockResponse.mockSubJob1.name))
        uiStateManager.uiState.test {
            var item = awaitItem()
            assertEquals(
                153,
                item.job.subJobs.first().staffs.first().assignedRows.first().assignedTrees
            )
        }
    }

    @Test
    fun `Test handle SwitchRateTypeAction`() = runTest {
        viewModel.handle(
            OrchardUpdateAction.SwitchRateTypeAction(
                MockResponse.mockStaff1,
                RateType.PieceRate
            )
        )
        uiStateManager.uiState.test {
            var item = awaitItem()
            assertEquals(
                RateType.PieceRate,
                item.job.subJobs.first().staffs.first().rateType
            )
        }
    }

    @Test
    fun `Test handle ToggleTreeRowAction`() = runTest {
        assertTrue(
            uiStateManager.uiState.value.job.subJobs.first().staffs.first().assignedRows
                .any { it.rowId == 3 }
                .not()
        )

        viewModel.handle(
            OrchardUpdateAction.ToggleTreeRowAction(
                MockResponse.mockStaff1,
                3
            )
        )
        uiStateManager.uiState.test {
            var item = awaitItem()
            assertTrue(
                item.job.subJobs.first().staffs.first().assignedRows.any { it.rowId == 3 }
            )
        }
    }

    @Test
    fun `Test handle UpdateTreesInRowAction`() = runTest {
        assertTrue(
            uiStateManager.uiState.value.job.subJobs.first().staffs.first().assignedRows
                .first { it.rowId == 4 }
                .assignedTrees == 2
        )

        viewModel.handle(
            OrchardUpdateAction.UpdateTreesInRowAction(
                MockResponse.mockStaff1,
                4,
                100
            )
        )
        uiStateManager.uiState.test {
            var item = awaitItem()
            assertTrue(
                item.job.subJobs.first().staffs.first().assignedRows
                    .first { it.rowId == 4 }
                    .assignedTrees == 100
            )
        }
    }

    @Test
    fun `Test handle RateChangedAction`() = runTest {
        assertEquals(
            35,
            uiStateManager.uiState.value.job.subJobs.first().staffs[1].rate
        )

        viewModel.handle(
            OrchardUpdateAction.RateChangedAction(
                MockResponse.mockStaff2,
                100
            )
        )
        uiStateManager.uiState.test {
            var item = awaitItem()
            assertEquals(
                100,
                item.job.subJobs.first().staffs[1].rate
            )
        }
    }

    @Test
    fun `Test handle ApplyRateToAllAction`() = runTest {
        assertEquals(
            35,
            uiStateManager.uiState.value.job.subJobs.first().staffs[1].rate
        )

        viewModel.handle(
            OrchardUpdateAction.SwitchRateTypeAction(
                MockResponse.mockStaff1,
                RateType.PieceRate
            )
        )

        viewModel.handle(
            OrchardUpdateAction.ApplyRateToAllAction(
                MockResponse.mockSubJob1,
                100
            )
        )
        uiStateManager.uiState.test {
            var item = awaitItem()
            assertEquals(
                100,
                item.job.subJobs.first().staffs[0].rate
            )
            assertEquals(
                100,
                item.job.subJobs.first().staffs[1].rate
            )
        }
    }

}