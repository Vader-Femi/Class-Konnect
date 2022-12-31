package com.femi.e_class.data.repository

import com.femi.e_class.data.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val collectionReference: CollectionReference,
    dataStore: UserPreferences
): SignUpRepository, BaseRepositoryImpl(dataStore) {

    override fun getAuthReference() = firebaseAuth
    override fun getCollectionReference() = collectionReference
}