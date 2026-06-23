package com.example.uasrpl_mitradarat.data.repository

import com.example.uasrpl_mitradarat.data.remote.BusDataSource
import com.example.uasrpl_mitradarat.domain.model.Bus
import com.example.uasrpl_mitradarat.domain.model.CrowdStatus
import com.example.uasrpl_mitradarat.domain.repository.BusRepository
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class BusRepositoryImpl(
    private val dataSource: BusDataSource,
) : BusRepository {

    override fun getAllBuses(): Flow<List<Bus>> = callbackFlow {
        val subscription = dataSource.busesCollection()
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val buses = snapshot.documents.mapNotNull { it.toBus() }
                    trySend(buses)
                }
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun getBusById(busId: String): Bus? {
        return dataSource
            .busesCollection()
            .document(busId)
            .get()
            .await()
            .toBus()
    }

    private fun DocumentSnapshot.toBus(): Bus? {
        return try {
            val busId = getString("busId") ?: id
            val busName = getString("busName") ?: ""
            val crowdStatusStr = getString("crowdStatus") ?: CrowdStatus.BELUM_ADA_DATA.name
            val crowdStatus = try {
                CrowdStatus.valueOf(crowdStatusStr)
            } catch (e: Exception) {
                CrowdStatus.BELUM_ADA_DATA
            }
            val lastUpdated = getLong("lastUpdated") ?: 0L
            
            Bus(busId, busName, crowdStatus, lastUpdated)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateCrowdStatus(busId: String, status: CrowdStatus) {
        dataSource
            .busesCollection()
            .document(busId)
            .update(
                mapOf(
                    "crowdStatus" to status.name,
                    "lastUpdated" to System.currentTimeMillis()
                )
            )
            .await()
    }
}