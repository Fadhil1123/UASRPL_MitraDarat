package com.example.uasrpl_mitradarat.data.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class BusDataSource(
    private val firestore: FirebaseFirestore
) {

    fun busesCollection(): CollectionReference =
        firestore.collection(FirestoreCollections.BUSES)
}