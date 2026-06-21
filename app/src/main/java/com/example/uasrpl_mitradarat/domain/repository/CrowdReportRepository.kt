package com.example.uasrpl_mitradarat.domain.repository

import com.example.uasrpl_mitradarat.domain.model.CrowdReport

interface CrowdReportRepository {

    suspend fun submitReport(report: CrowdReport)

    suspend fun getReportsByBus(busId: String): List<CrowdReport>

    suspend fun getLatestReportByUserAndBus(
        userId: String,
        busId: String
    ): CrowdReport?
}