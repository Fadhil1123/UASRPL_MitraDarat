package com.example.uasrpl_mitradarat.domain.usecase

import com.example.uasrpl_mitradarat.domain.model.CrowdStatus
import com.example.uasrpl_mitradarat.domain.repository.BusRepository

class UpdateBusCrowdStatusUseCase(
        private val repository: BusRepository
) {

    suspend operator fun invoke(
            busId: String,
            status: CrowdStatus
    ) {

        repository.updateCrowdStatus(
                busId,
                status
        )
    }
}