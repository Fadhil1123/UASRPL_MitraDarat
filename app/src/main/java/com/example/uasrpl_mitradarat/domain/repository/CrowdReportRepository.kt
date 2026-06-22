package com.example.uasrpl_mitradarat.domain.repository

import com.example.uasrpl_mitradarat.domain.model.CrowdReport
import kotlinx.coroutines.flow.Flow

interface CrowdReportRepository {

    suspend fun submitReport(report: CrowdReport)

    suspend fun getReportsByBus(busId: String): List<CrowdReport>

    fun getReportsByBusStream(busId: String): Flow<List<CrowdReport>>

    suspend fun getLatestReportByUserAndBus(
        userId: String,
        busId: String
    ): CrowdReport?
}