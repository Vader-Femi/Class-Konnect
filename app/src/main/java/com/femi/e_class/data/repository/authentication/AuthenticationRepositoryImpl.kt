package com.femi.e_class.data.repository.authentication

import com.femi.e_class.data.UserPreferences
import com.femi.e_class.data.repository.base.BaseRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val collectionReference: CollectionReference,
    dataStore: UserPreferences
): AuthenticationRepository, BaseRepositoryImpl(dataStore) {

    override fun getAuthReference() = firebaseAuth
    override fun getCollectionReference() = collectionReference
}