package io.trieulh.challenge.ui.screen.orchard.update

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.trieulh.challenge.data.MockResponse
import io.trieulh.challenge.domain.model.Job
import io.trieulh.challenge.ui.common.ChallengeTopAppBar
import io.trieulh.challenge.ui.screen.orchard.update.component.OrchardJobItem
import io.trieulh.challenge.R
import io.trieulh.challenge.domain.model.SubJob

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun OrchardUpdateScreen() {
    Scaffold(
        topBar = {
            UpdateTopBar()
        },
        content = { UpdateContent() }
    )
}

@Composable
fun UpdateTopBar() {
    ChallengeTopAppBar(
        onNavigationClick = { /*TODO*/ },
        title = stringResource(id = R.string.orchard_update_title)
    )
}

@Composable
fun UpdateContent(job: Job = MockResponse.mockJob) {
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
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
        Button(
            onClick = { /*TODO*/ }, modifier = Modifier
                .padding(vertical = 24.dp, horizontal = 36.dp)
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = stringResource(id = R.string.confirm).uppercase())
        }

    }

}

@Preview
@Composable
fun PreviewOrchardUpdateScreen() {
    OrchardUpdateScreen()
}
