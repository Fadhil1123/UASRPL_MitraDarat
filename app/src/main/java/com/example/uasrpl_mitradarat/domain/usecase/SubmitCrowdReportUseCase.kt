package com.example.uasrpl_mitradarat.domain.usecase

import com.example.uasrpl_mitradarat.domain.model.CrowdReport
import com.example.uasrpl_mitradarat.domain.model.CrowdStatus
import com.example.uasrpl_mitradarat.domain.repository.CrowdReportRepository
import kotlinx.coroutines.flow.first
import java.util.UUID

class SubmitCrowdReportUseCase(
    private val reportRepository: CrowdReportRepository,
    private val checkCooldownUseCase: CheckCooldownUseCase,
    private val getActiveReportsUseCase: GetActiveReportsUseCase,
    private val calculateCrowdDensityUseCase: CalculateCrowdDensityUseCase,
    private val updateBusCrowdStatusUseCase: UpdateBusCrowdStatusUseCase,
) {

    suspend operator fun invoke(
        userId: String,
        busId: String,
        status: CrowdStatus
    ): Result<Unit> {

        val canSubmit = checkCooldownUseCase(
            userId,
            busId
        )

        if (!canSubmit) {
            return Result.failure(
                Exception("Anda hanya dapat mengirim laporan setiap 10 menit")
            )
        }

        val report = CrowdReport(
            reportId = UUID.randomUUID().toString(),
            busId = busId,
            userId = userId,
            status = status,
            timestamp = System.currentTimeMillis()
        )

        reportRepository.submitReport(report)

        val activeReports = getActiveReportsUseCase(busId).first()

        val crowdStatus = calculateCrowdDensityUseCase(activeReports)

        updateBusCrowdStatusUseCase(
            busId,
            crowdStatus
        )

        return Result.success(Unit)
    }
}