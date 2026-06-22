package com.example.uasrpl_mitradarat.data.repository

import com.example.uasrpl_mitradarat.data.remote.CrowdReportDataSource
import com.example.uasrpl_mitradarat.domain.model.CrowdReport
import com.example.uasrpl_mitradarat.domain.repository.CrowdReportRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class CrowdReportRepositoryImpl(
    private val dataSource: CrowdReportDataSource,
) : CrowdReportRepository {

    override suspend fun submitReport(
        report: CrowdReport
    ) {
        dataSource
            .reportsCollection()
            .document(report.reportId)
            .set(report)
            .await()
    }

    override suspend fun getReportsByBus(
        busId: String
    ): List<CrowdReport> {
        return dataSource
            .reportsCollection()
            .whereEqualTo("busId", busId)
            .get()
            .await()
            .toObjects(CrowdReport::class.java)
    }

    override fun getReportsByBusStream(busId: String): Flow<List<CrowdReport>> = callbackFlow {
        val subscription = dataSource.reportsCollection()
            .whereEqualTo("busId", busId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val reports = snapshot.toObjects(CrowdReport::class.java)
                    trySend(reports)
                }
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun getLatestReportByUserAndBus(
        userId: String,
        busId: String
    ): CrowdReport? {

        return dataSource
            .reportsCollection()
            .whereEqualTo("userId", userId)
            .whereEqualTo("busId", busId)
            .orderBy("timestamp")
            .limitToLast(1)
            .get()
            .await()
            .toObjects(CrowdReport::class.java)
            .firstOrNull()
    }
}