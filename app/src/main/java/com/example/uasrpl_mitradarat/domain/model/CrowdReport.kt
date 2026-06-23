package com.example.uasrpl_mitradarat.domain.model

data class CrowdReport(
    val reportId: String = "",
    val busId: String = "",
    val userId: String = "",
    val status: CrowdStatus = CrowdStatus.BELUM_ADA_DATA,
    val timestamp: Long = 0L
)
