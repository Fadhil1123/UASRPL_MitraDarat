package com.example.uasrpl_mitradarat.data.repository

import com.example.uasrpl_mitradarat.data.remote.BusDataSource
import com.example.uasrpl_mitradarat.domain.model.Bus
import com.example.uasrpl_mitradarat.domain.model.CrowdStatus
import com.example.uasrpl_mitradarat.domain.repository.BusRepository
import kotlinx.coroutines.tasks.await

class BusRepositoryImpl(
    private val dataSource: BusDataSource,
) : BusRepository {

    override suspend fun getBusById(busId: String): Bus? {
        return dataSource
            .busesCollection()
            .document(busId)
            .get()
            .await()
            .toObject(Bus::class.java)
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