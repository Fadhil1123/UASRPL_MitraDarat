package com.example.uasrpl_mitradarat.domain.usecase

import com.example.uasrpl_mitradarat.domain.model.CrowdReport
import com.example.uasrpl_mitradarat.domain.model.CrowdStatus

class CalculateCrowdDensityUseCase {

    operator fun invoke(
        reports: List<CrowdReport>
    ): CrowdStatus {

        if (reports.isEmpty()) {
            return CrowdStatus.NETRAL
        }

        val total = reports.sumOf {

            when (it.status) {
                CrowdStatus.LONGGAR -> 1
                CrowdStatus.SEDANG -> 2
                CrowdStatus.PADAT -> 3
                CrowdStatus.NETRAL -> 0
            }
        }

        val average = total.toDouble() / reports.size

        return when {
            average <= 1.66 -> CrowdStatus.LONGGAR
            average <= 2.33 -> CrowdStatus.SEDANG
            else -> CrowdStatus.PADAT
        }
    }
}