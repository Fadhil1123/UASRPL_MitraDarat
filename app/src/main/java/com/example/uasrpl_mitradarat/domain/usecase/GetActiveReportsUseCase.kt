package com.example.uasrpl_mitradarat.domain.usecase

import com.example.uasrpl_mitradarat.domain.model.CrowdReport
import com.example.uasrpl_mitradarat.domain.repository.CrowdReportRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class GetActiveReportsUseCase(
    private val repository: CrowdReportRepository,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(busId: String): Flow<List<CrowdReport>> {
        return repository.getReportsByBusStream(busId).flatMapLatest { reports ->
            // Emit filtered reports, and re-emit every 30 seconds to handle expiry
            // even if no new data comes from Firestore
            flow {
                while (true) {
                    val threshold = System.currentTimeMillis() - (10 * 60 * 1000)
                    val activeReports = reports
                        .filter { it.timestamp >= threshold }
                        .sortedByDescending { it.timestamp }
                        .distinctBy { it.userId }

                    emit(activeReports)

                    // If all reports expired, we can stop the loop or just wait
                    if (activeReports.isEmpty() && reports.all { it.timestamp < threshold }) {
                        // All existing reports in DB for this bus are expired.
                        // We still wait for potential new reports from the parent stream.
                        delay(60000)
                    } else {
                        delay(30000) // Re-check every 30s
                    }
                }
            }
        }
    }
}