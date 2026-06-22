package com.example.uasrpl_mitradarat.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreTester {
    private const val TAG = "FIRESTORE_TEST"

    fun runHealthCheck() {
        val db = FirebaseFirestore.getInstance()
        val testDoc = hashMapOf(
            "status" to "connected",
            "timestamp" to System.currentTimeMillis(),
            "message" to "Testing connection from Android App"
        )

        Log.d(TAG, "Starting Firestore Connection Test...")

        db.collection("connection_test")
            .add(testDoc)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "SUCCESS: Document added with ID: ${documentReference.id}")
                Log.d(TAG, "Check your Firebase Console -> Firestore -> connection_test collection")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "ERROR: Connection failed", e)
                Log.e(TAG, "Reason: ${e.localizedMessage}")
            }
    }
}
