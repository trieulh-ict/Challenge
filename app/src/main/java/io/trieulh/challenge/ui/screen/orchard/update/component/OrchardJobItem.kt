package io.trieulh.challenge.ui.screen.orchard.update.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.trieulh.challenge.R
import io.trieulh.challenge.data.MockResponse
import io.trieulh.challenge.domain.model.RateType
import io.trieulh.challenge.domain.model.Staff
import io.trieulh.challenge.domain.model.SubJob
import io.trieulh.challenge.ui.theme.LightTaupe
import io.trieulh.challenge.ui.theme.OldLace

@Composable
fun OrchardJobItem(
    subJob: SubJob,
    jobName: String,
    onUpdateMaxTrees: () -> Unit = {},
    onSwitchRateType: (Staff, RateType) -> Unit = { staff, rateType -> },
    onToggleTreeRow: (Staff, Int) -> Unit = { staff, rowId -> },
    onUpdateTreesInRow: (Staff, Int, Int) -> Unit = { staff, rowId, treeNumber -> },
    onRateChanged: (Staff, Int) -> Unit = { staff, rate -> },
    onApplyRateToAll: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        elevation = 8.dp,
        modifier = modifier.background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            HeaderItem(subJob.name, onUpdateMaxTrees)
            subJob.staffs.forEachIndexed { index, staff ->
                OrchardWorkerItem(
                    staff,
                    jobName,
                    subJob.availableRows,
                    onSwitchRateType,
                    onToggleTreeRow,
                    onUpdateTreesInRow,
                    onRateChanged,
                    onApplyRateToAll
                )
                if (index < subJob.staffs.lastIndex)
                    Divider(color = Color.LightGray, thickness = 1.dp)
            }
        }
    }
}

@Composable
private fun HeaderItem(jobName: String, onUpdateMaxTrees: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(OldLace)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = jobName,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.weight(1f)
        )
        OutlinedButton(
            onClick = onUpdateMaxTrees,
            border = BorderStroke(1.dp, color = LightTaupe),
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = Color.Transparent
            )
        ) {
            Text(
                text = stringResource(id = R.string.orchard_update_btn_add_max_trees),
                color = LightTaupe,
                fontSize = 10.sp
            )
        }
    }
}

@Preview
@Composable
private fun PreviewOrchardJobItem() {
    OrchardJobItem(MockResponse.mockSubJob1, MockResponse.mockJob.name)
}
