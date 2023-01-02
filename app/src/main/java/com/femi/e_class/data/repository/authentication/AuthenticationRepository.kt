package com.femi.e_class.data.repository.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference

interface AuthenticationRepository {
    fun getAuthReference(): FirebaseAuth
    fun getCollectionReference(): CollectionReference
}