package com.example.uasrpl_mitradarat.domain.model

data class Bus(
    val busId: String = "",
    val busName: String = "",
    val crowdStatus: CrowdStatus = CrowdStatus.NETRAL,
    val lastUpdated: Long = 0L
)