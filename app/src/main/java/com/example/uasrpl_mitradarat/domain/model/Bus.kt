package com.example.uasrpl_mitradarat.domain.model

data class Bus(
    val busId: String = "",
    val busName: String = "",
    val crowdStatus: CrowdStatus = CrowdStatus.BELUM_ADA_DATA,
    val lastUpdated: Long = 0L
)
