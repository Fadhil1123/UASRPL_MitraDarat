package com.example.uasrpl_mitradarat.data

import android.util.Log
import com.example.uasrpl_mitradarat.domain.model.Bus
import com.example.uasrpl_mitradarat.domain.model.CrowdStatus
import com.google.firebase.firestore.FirebaseFirestore

object SeedDataUtility {
    private const val TAG = "SEED_DATA"

    fun seedInitialBuses() {
        val db = FirebaseFirestore.getInstance()
        val buses = listOf(
            Bus("TB IA 05", "Batik Solo Trans", CrowdStatus.LONGGAR, System.currentTimeMillis()),
            Bus("TB IIB 01", "Batik Solo Trans", CrowdStatus.SEDANG, System.currentTimeMillis()),
            Bus("TB IC 03", "Trans Banjarbakula", CrowdStatus.LONGGAR, System.currentTimeMillis()),
            Bus("TB IB 02", "Trans Banjarbakula", CrowdStatus.SEDANG, System.currentTimeMillis()),
            Bus("TB IB 01", "Trans Banjarbakula", CrowdStatus.PADAT, System.currentTimeMillis()),
            Bus("TB IIA 12", "Balikpapan City Trans", CrowdStatus.PADAT, System.currentTimeMillis()),
            Bus("TB IIIA 04", "Metro Jabar Trans", CrowdStatus.LONGGAR, System.currentTimeMillis()),
            Bus("TB IIA 09", "Trans Musi", CrowdStatus.SEDANG, System.currentTimeMillis()),
            Bus("TB IVA 02", "Trans Manado", CrowdStatus.LONGGAR, System.currentTimeMillis())
        )

        val batch = db.batch()
        buses.forEach { bus ->
            val docRef = db.collection("buses").document(bus.busId)
            batch.set(docRef, bus)
        }

        batch.commit()
            .addOnSuccessListener {
                Log.d(TAG, "Successfully seeded ${buses.size} buses to Firestore")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to seed buses", e)
            }
    }
}
