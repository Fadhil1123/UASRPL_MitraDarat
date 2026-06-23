package com.example.uasrpl_mitradarat.data.repository

import com.example.uasrpl_mitradarat.data.remote.CrowdReportDataSource
import com.example.uasrpl_mitradarat.domain.model.CrowdReport
import com.example.uasrpl_mitradarat.domain.model.CrowdStatus
import com.example.uasrpl_mitradarat.domain.repository.CrowdReportRepository
import com.google.firebase.firestore.DocumentSnapshot
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
        val reportData = mapOf(
            "reportId" to report.reportId,
            "busId" to report.busId,
            "userId" to report.userId,
            "status" to report.status.name, // Explicitly use name for Firestore
            "timestamp" to report.timestamp
        )
        
        dataSource
            .reportsCollection()
            .document(report.reportId)
            .set(reportData)
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
            .documents.mapNotNull { it.toCrowdReport() }
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
                    val reports = snapshot.documents.mapNotNull { it.toCrowdReport() }
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
            .documents.firstOrNull()?.toCrowdReport()
    }

    private fun DocumentSnapshot.toCrowdReport(): CrowdReport? {
        return try {
            val reportId = getString("reportId") ?: id
            val busId = getString("busId") ?: ""
            val userId = getString("userId") ?: ""
            val statusStr = getString("status") ?: CrowdStatus.BELUM_ADA_DATA.name
            val status = try {
                CrowdStatus.valueOf(statusStr)
            } catch (e: Exception) {
                CrowdStatus.BELUM_ADA_DATA
            }
            val timestamp = getLong("timestamp") ?: 0L
            
            CrowdReport(reportId, busId, userId, status, timestamp)
        } catch (e: Exception) {
            null
        }
    }
}
