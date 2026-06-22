package com.example.uasrpl_mitradarat.domain.usecase

import com.example.uasrpl_mitradarat.domain.model.CrowdReport
import com.example.uasrpl_mitradarat.domain.repository.CrowdReportRepository

class GetActiveReportsUseCase(
    private val repository: CrowdReportRepository
) {

    suspend operator fun invoke(
        busId: String
    ): List<CrowdReport> {

        val reports =
            repository.getReportsByBus(busId)

        val threshold =
            System.currentTimeMillis() -
                    (10 * 60 * 1000)

        return reports
            .filter { it.timestamp >= threshold }
            .sortedByDescending { it.timestamp }
            .distinctBy { it.userId }
    }
}