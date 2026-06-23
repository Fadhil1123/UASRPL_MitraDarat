package com.example.uasrpl_mitradarat.domain.repository

import com.example.uasrpl_mitradarat.domain.model.Bus
import com.example.uasrpl_mitradarat.domain.model.CrowdStatus
import kotlinx.coroutines.flow.Flow

interface BusRepository {

    fun getAllBuses(): Flow<List<Bus>>

    suspend fun getBusById(
        busId: String
    ): Bus?

    suspend fun updateCrowdStatus(
        busId: String,
        status: CrowdStatus
    )
}