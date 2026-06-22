package com.example.uasrpl_mitradarat.domain.model

data class CrowdReport(
    val reportId: String = "",
    val busId: String = "",
    val userId: String = "",
    val status: CrowdStatus = CrowdStatus.NETRAL,
    val timestamp: Long = 0L
)