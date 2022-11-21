package com.femi.e_class.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference

class SignUpRepository(
    private val firebaseAuth: FirebaseAuth,
    private val collectionReference: CollectionReference,
) : BaseRepository() {

    fun getCollectionReference() = collectionReference

    fun getAuthReference() = firebaseAuth
}