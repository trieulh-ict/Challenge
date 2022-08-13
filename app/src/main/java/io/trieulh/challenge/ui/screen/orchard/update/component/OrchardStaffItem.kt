package io.trieulh.challenge.ui.screen.orchard.update.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.trieulh.challenge.data.MockResponse
import io.trieulh.challenge.domain.model.AvailableRow
import io.trieulh.challenge.domain.model.RateType
import io.trieulh.challenge.domain.model.Staff
import io.trieulh.challenge.ui.theme.CelticBlue
import io.trieulh.challenge.ui.theme.LightTaupe
import io.trieulh.challenge.ui.theme.SatinSheenGold
import io.trieulh.challenge.R
import io.trieulh.challenge.domain.model.AssignedRow

@Composable
fun OrchardWorkerItem(
    staff: Staff,
    jobName: String,
    availableRows: List<AvailableRow>,
    onSwitchRateType: (Staff, RateType) -> Unit,
    onToggleTreeRow: (Staff, Int) -> Unit,
    onUpdateTreesInRow: (Staff, Int, Int) -> Unit,
    onRateChanged: (Staff, Int) -> Unit,
    onApplyRateToAll: (Int) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(LightTaupe, shape = CircleShape)
                    .width(48.dp)
                    .height(48.dp)
            ) {
                Text(
                    text = staff.name.split(" ").map { it.first() }.joinToString(separator = "")
                        .uppercase()
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = staff.name, fontWeight = FontWeight.Medium)
        }

        Spacer(modifier = Modifier.height(8.dp))
        LabelRow(label = stringResource(id = R.string.orchard), value = staff.orchard)
        Spacer(modifier = Modifier.height(4.dp))
        LabelRow(label = stringResource(id = R.string.block), value = staff.block)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.rate_type),
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth()) {
            RateTypeButton(
                text = stringResource(id = R.string.piece_rate),
                selected = staff.rateType == RateType.PieceRate,
                modifier = Modifier.weight(1f),
                onClick = {
                    if (staff.rateType != RateType.PieceRate) {
                        onSwitchRateType(staff, RateType.PieceRate)
                    }
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            RateTypeButton(
                text = stringResource(id = R.string.wages),
                selected = staff.rateType == RateType.Wages,
                modifier = Modifier.weight(1f),
                onClick = {
                    if (staff.rateType != RateType.Wages) {
                        onSwitchRateType(staff, RateType.Wages)
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (staff.rateType == RateType.Wages) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.orchard_update_wages_description, jobName),
                color = SatinSheenGold
            )
        } else {
            PieceRateInputField(
                rate = staff.rate,
                onRateChange = {
                    onRateChanged(staff, it.toIntOrNull() ?: 0)
                },
                onApplyRateToAll = onApplyRateToAll
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            Modifier
                .fillMaxWidth()
        ) {
            items(availableRows, { row -> row.rowId }) { row ->
                AvailableRowItem(
                    row = row,
                    selected = row.rowId in staff.assignedRows.map { it.rowId },
                    donePartially = row.completedLogs.isNotEmpty(),
                    onToggle = {
                        onToggleTreeRow(staff, row.rowId)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        staff.assignedRows.forEach { assignedRow ->
            TreeInputTextField(
                assignedRow,
                availableRows.first { it.rowId == assignedRow.rowId },
                onValueChange = {
                    onUpdateTreesInRow(staff, assignedRow.rowId, it.toIntOrNull() ?: 0)
                }
            )
        }
    }
}

@Composable
fun TreeInputTextField(
    assignedRow: AssignedRow,
    rowInfo: AvailableRow,
    onValueChange: (String) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.orchard_update_tree_input_label, assignedRow.rowId),
            style = MaterialTheme.typography.caption.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(4.dp)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = assignedRow.assignedTrees.toString(),
                onValueChange = onValueChange,
                textStyle = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight.Bold
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.width(IntrinsicSize.Min)
            )
            Text(
                text = stringResource(R.string.orchard_update_maximum_trees, rowInfo.totalTrees.toString()),
                color = Color.LightGray,
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight.Light
                )
            )
        }
        rowInfo.completedLogs.forEach {
            Text(
                text = stringResource(R.string.orchard_update_completed, it.staffName, it.completed.toString()),
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(vertical = 4.dp),
                style = MaterialTheme.typography.caption.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun AvailableRowItem(
    row: AvailableRow,
    selected: Boolean = true,
    onToggle: () -> Unit = {},
    donePartially: Boolean = false
) {
    Box(
        modifier = Modifier
            .padding(end = 16.dp)
            .width(48.dp)
            .height(48.dp)
            .background(
                color = if (selected) CelticBlue else Color.LightGray,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(onClick = onToggle),
        contentAlignment = Alignment.Center
    ) {
        Text(text = row.rowId.toString(), color = if (selected) Color.White else Color.Black)
        if (donePartially)
            Box(
                modifier = Modifier
                    .offset(x = 16.dp, y = (-16).dp)
                    .size(4.dp)
                    .background(
                        color = Color.Red, shape = CircleShape
                    )
            )
    }
}

@Composable
private fun PieceRateInputField(
    rate: Int?,
    onRateChange: (String) -> Unit,
    onApplyRateToAll: (Int) -> Unit
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = (rate ?: 0).toString(),
            onValueChange = onRateChange,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            label = {
                Text(
                    text = stringResource(R.string.rate),
                    modifier = Modifier.background(Color.White)
                )
            },
            textStyle = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight.Normal
            ),
            leadingIcon = {
                Text(
                    text = stringResource(R.string.sign_usd),
                    color = Color.Gray,
                    style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
            },
            trailingIcon = {
                Text(
                    text = stringResource(R.string.per_hour),
                    color = Color.Gray,
                    modifier = Modifier.padding(end = 16.dp),
                    style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
            },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(4.dp))
        TextButton(onClick = {
            onApplyRateToAll(rate ?: 0)
        }) {
            Text(stringResource(R.string.orchard_update_btn_apply_to_all))
        }
    }
}

@Composable
private fun RateTypeButton(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .background(
                color = if (selected) LightTaupe else Color.LightGray,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(text.uppercase(), color = if (selected) Color.White else Color.Black)
    }
}

@Composable
private fun LabelRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth()) {
        Text(text = label, style = MaterialTheme.typography.caption, color = Color.Gray)
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = value, style = MaterialTheme.typography.caption, fontWeight = FontWeight.Medium)
    }
}

@Preview
@Composable
private fun PreviewOrchardWorkerItem() {
    OrchardWorkerItem(
        MockResponse.mockStaff2,
        MockResponse.mockJob.name,
        MockResponse.mockSubJob1.availableRows,
        onSwitchRateType = { staff, rateType -> },
        onToggleTreeRow = { staff, rowId -> },
        onUpdateTreesInRow = { staff, rowId, treeNumber -> },
        onRateChanged = { staff, rate -> },
        onApplyRateToAll = { rate -> }
    )
}