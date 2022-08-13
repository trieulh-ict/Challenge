package io.trieulh.challenge.ui.screen.orchard.update

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import io.trieulh.challenge.R
import io.trieulh.challenge.data.MockResponse
import io.trieulh.challenge.domain.model.Job
import io.trieulh.challenge.domain.model.RateType
import io.trieulh.challenge.domain.model.Staff
import io.trieulh.challenge.ui.common.ChallengeTopAppBar
import io.trieulh.challenge.ui.screen.orchard.update.component.OrchardJobItem
import io.trieulh.challenge.ui.screen.orchard.update.destinations.OrchardUpdateSuccessScreenDestination
import io.trieulh.challenge.ui.theme.AnimatedTransition
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@RootNavGraph(start = true)
@Destination
@Composable
fun OrchardUpdateScreen(
    navigator: DestinationsNavigator,
    viewModel: OrchardUpdateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(navigator) {
        viewModel.navigationEvent.collectLatest {
            when (it) {
                OrchardUpdateNavigationState.NavigateToSuccess -> {
                    navigator.navigate(OrchardUpdateSuccessScreenDestination) {
                        launchSingleTop = true
                        popUpTo(NavGraphs.root)
                    }
                }
                null -> {}
            }
        }
    }

    Scaffold(
        topBar = { UpdateTopBar() },
        content = {
            if (uiState.isFetchingData) {
                UpdateLoading()
            } else {
                UpdateContent(
                    job = uiState.job,
                    isSubmittingData = uiState.isSubmittingData,
                    uiStateHandler = viewModel,
                    onSubmit = {
                        viewModel.submitData()
                    }
                )
            }
        }
    )
}

@Composable
private fun UpdateLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun UpdateTopBar() {
    ChallengeTopAppBar(
        onNavigationClick = { /*TODO*/ },
        title = stringResource(id = R.string.orchard_update_title)
    )
}

@Composable
private fun UpdateContent(
    job: Job,
    isSubmittingData: Boolean,
    onSubmit: () -> Unit,
    uiStateHandler: OrchardUpdateUiStateManager?
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            job.subJobs.forEach { subJob ->
                OrchardJobItem(
                    subJob = subJob,
                    jobName = job.name,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    onUpdateMaxTrees = {
                        uiStateHandler?.updateMaxTreesBySubJob(subJob.name)
                    },
                    onSwitchRateType = { staff: Staff, rateType: RateType ->
                        uiStateHandler?.switchRateType(staff, rateType)
                    },
                    onToggleTreeRow = { staff: Staff, rowId: Int ->
                        uiStateHandler?.onToggleTreeRow(staff, rowId)
                    },
                    onUpdateTreesInRow = { staff: Staff, rowId: Int, treeNumber: Int ->
                        uiStateHandler?.onUpdateTreesInRow(staff, rowId, treeNumber)
                    },
                    onRateChanged = { staff: Staff, rate: Int ->
                        uiStateHandler?.onRateChanged(staff, rate)
                    },
                    onApplyRateToAll = { rate: Int ->
                        uiStateHandler?.onAppRateToAll(subJob, rate)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
        Button(
            onClick = onSubmit,
            enabled = !isSubmittingData,
            modifier = Modifier
                .padding(vertical = 24.dp, horizontal = 36.dp)
                .fillMaxWidth()
                .height(48.dp)
        ) {
            if (isSubmittingData) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
            }
            Text(text = stringResource(id = R.string.confirm).uppercase())
        }

    }

}

@Preview
@Composable
private fun PreviewOrchardUpdateScreen() {
    UpdateContent(
        job = MockResponse.mockJob,
        isSubmittingData = true,
        uiStateHandler = null,
        onSubmit = {}
    )
}
