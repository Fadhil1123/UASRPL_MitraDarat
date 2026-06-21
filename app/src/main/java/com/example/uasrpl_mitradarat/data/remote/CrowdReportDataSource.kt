package com.example.uasrpl_mitradarat.data.remote

import com.google.firebase.firestore.FirebaseFirestore

class CrowdReportDataSource(
    private val firestore: FirebaseFirestore
) {

    fun reportsCollection() =
        firestore.collection(FirestoreCollections.CROWD_REPORTS)

}