package com.example.uasrpl_mitradarat.domain.usecase

import com.example.uasrpl_mitradarat.domain.repository.CrowdReportRepository

class CheckCooldownUseCase(
    private val repository: CrowdReportRepository
) {

    suspend operator fun invoke(
        userId: String,
        busId: String
    ): Boolean {

        val latestReport =
            repository.getLatestReportByUserAndBus(
                userId,
                busId
            ) ?: return true

        val cooldownPeriod =
            2 * 60 * 1000L

        return System.currentTimeMillis() -
                latestReport.timestamp >= cooldownPeriod
    }
}