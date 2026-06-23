package com.example.uasrpl_mitradarat.data

import com.example.uasrpl_mitradarat.data.remote.BusDataSource
import com.example.uasrpl_mitradarat.data.remote.CrowdReportDataSource
import com.example.uasrpl_mitradarat.data.repository.BusRepositoryImpl
import com.example.uasrpl_mitradarat.data.repository.CrowdReportRepositoryImpl
import com.example.uasrpl_mitradarat.domain.repository.BusRepository
import com.example.uasrpl_mitradarat.domain.repository.CrowdReportRepository
import com.example.uasrpl_mitradarat.domain.usecase.*
import com.google.firebase.firestore.FirebaseFirestore

interface AppContainer {
    val busRepository: BusRepository
    val crowdReportRepository: CrowdReportRepository
    
    // UseCases
    val checkCooldownUseCase: CheckCooldownUseCase
    val calculateCrowdDensityUseCase: CalculateCrowdDensityUseCase
    val getActiveReportsUseCase: GetActiveReportsUseCase
    val updateBusCrowdStatusUseCase: UpdateBusCrowdStatusUseCase
    val submitCrowdReportUseCase: SubmitCrowdReportUseCase
    val observeBusCrowdStatusUseCase: ObserveBusCrowdStatusUseCase
}

class DefaultAppContainer : AppContainer {
    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val busDataSource: BusDataSource by lazy {
        BusDataSource(firestore)
    }

    private val crowdReportDataSource: CrowdReportDataSource by lazy {
        CrowdReportDataSource(firestore)
    }

    override val busRepository: BusRepository by lazy {
        BusRepositoryImpl(busDataSource)
    }

    override val crowdReportRepository: CrowdReportRepository by lazy {
        CrowdReportRepositoryImpl(crowdReportDataSource)
    }

    // UseCase Implementations
    override val checkCooldownUseCase: CheckCooldownUseCase by lazy {
        CheckCooldownUseCase(crowdReportRepository)
    }

    override val calculateCrowdDensityUseCase: CalculateCrowdDensityUseCase by lazy {
        CalculateCrowdDensityUseCase()
    }

    override val getActiveReportsUseCase: GetActiveReportsUseCase by lazy {
        GetActiveReportsUseCase(crowdReportRepository)
    }

    override val updateBusCrowdStatusUseCase: UpdateBusCrowdStatusUseCase by lazy {
        UpdateBusCrowdStatusUseCase(busRepository)
    }

    override val submitCrowdReportUseCase: SubmitCrowdReportUseCase by lazy {
        SubmitCrowdReportUseCase(
            crowdReportRepository,
            checkCooldownUseCase,
            getActiveReportsUseCase,
            calculateCrowdDensityUseCase,
            updateBusCrowdStatusUseCase
        )
    }

    override val observeBusCrowdStatusUseCase: ObserveBusCrowdStatusUseCase by lazy {
        ObserveBusCrowdStatusUseCase(
            getActiveReportsUseCase,
            calculateCrowdDensityUseCase
        )
    }
}
