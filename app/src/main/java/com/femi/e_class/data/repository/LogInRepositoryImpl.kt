package com.femi.e_class.data.repository

import com.femi.e_class.data.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class LogInRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val collectionReference: CollectionReference,
    dataStore: UserPreferences
) : LogInRepository, BaseRepositoryImpl(dataStore) {

    override fun getAuthReference() = firebaseAuth
    override fun getCollectionReference() = collectionReference
}