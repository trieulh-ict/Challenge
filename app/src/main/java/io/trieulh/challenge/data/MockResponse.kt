package io.trieulh.challenge.data

import io.trieulh.challenge.domain.model.*

object MockResponse {
    val mockStaff1 = Staff(
        assignedRows = listOf(
            AssignedRow(
                assignedTrees = 2,
                rowId = 4
            )
        ),
        name = "Barco",
        rate = null,
        rateType = RateType.Wages,
        orchard = "Benji (V1394U)",
        block = "UB12"
    )

    val mockStaff2 = Staff(
        assignedRows = listOf(
            AssignedRow(
                assignedTrees = 2,
                rowId = 4
            ),
            AssignedRow(
                assignedTrees = 20,
                rowId = 5
            )
        ),
        name = "Henry Pham",
        rate = 35,
        rateType = RateType.PieceRate,
        orchard = "Benji (V1394U)",
        block = "UB13"
    )

    val mockStaff3 = Staff(
        assignedRows = listOf(
            AssignedRow(
                assignedTrees = 2,
                rowId = 4
            ),
            AssignedRow(
                assignedTrees = 20,
                rowId = 5
            )
        ),
        name = "Darijan",
        rate = 35,
        rateType = RateType.PieceRate,
        orchard = "Benji (V1394U)",
        block = "UB14"
    )

    val mockSubJob1 = SubJob(
        name = "Pruning",
        availableRows = listOf(
            AvailableRow(
                completedLogs = listOf(),
                rowId = 3,
                totalTrees = 200,
            ),
            AvailableRow(
                completedLogs = listOf(
                    CompletedLog(
                        completed = 250,
                        staffName = "Yi Wan"
                    )
                ),
                rowId = 4,
                totalTrees = 556,
            ),
            AvailableRow(
                completedLogs = listOf(
                    CompletedLog(
                        completed = 100,
                        staffName = "Elizabeth Jargrave"
                    )
                ),
                rowId = 5,
                totalTrees = 270,
            )
        ),
        staffs = listOf(
            mockStaff1,
            mockStaff2
        )
    )

    val mockSubJob2 = SubJob(
        name = "Thining",
        availableRows = listOf(
            AvailableRow(
                completedLogs = listOf(),
                rowId = 3,
                totalTrees = 200,
            ),
            AvailableRow(
                completedLogs = listOf(
                    CompletedLog(
                        completed = 250,
                        staffName = "Yi Wan"
                    )
                ),
                rowId = 4,
                totalTrees = 556,
            ),
            AvailableRow(
                completedLogs = listOf(
                    CompletedLog(
                        completed = 100,
                        staffName = "Elizabeth Jargrave"
                    )
                ),
                rowId = 5,
                totalTrees = 270,
            )
        ),
        staffs = listOf(
            mockStaff3
        )
    )

    val mockJob = Job(
        name = "Canker Removal",
        subJobs = listOf(
            mockSubJob1,
            mockSubJob2
        )
    )
}