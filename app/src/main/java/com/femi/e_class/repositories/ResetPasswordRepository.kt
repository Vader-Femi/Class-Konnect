package com.femi.e_class.repositories

import com.femi.e_class.data.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference

class ResetPasswordRepository(
    private val firebaseAuth: FirebaseAuth,
    dataStore: UserPreferences
) : BaseRepository(dataStore) {
    fun getAuthReference() = firebaseAuth
}