package com.example.uasrpl_mitradarat.domain.usecase

import com.example.uasrpl_mitradarat.domain.model.CrowdStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveBusCrowdStatusUseCase(
    private val getActiveReportsUseCase: GetActiveReportsUseCase,
    private val calculateCrowdDensityUseCase: CalculateCrowdDensityUseCase,
) {

    operator fun invoke(busId: String): Flow<CrowdStatus> {
        return getActiveReportsUseCase(busId).map { activeReports ->
            calculateCrowdDensityUseCase(activeReports)
        }
    }
}