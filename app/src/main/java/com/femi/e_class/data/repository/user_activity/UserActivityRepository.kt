package com.femi.e_class.data.repository.user_activity

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference

interface UserActivityRepository {
    fun getAuthReference(): FirebaseAuth
    fun getCollectionReference(): CollectionReference
}