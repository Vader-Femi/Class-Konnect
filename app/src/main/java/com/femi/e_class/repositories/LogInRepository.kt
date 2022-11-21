package com.femi.e_class.repositories

import com.google.firebase.auth.FirebaseAuth

class LogInRepository(
    private val firebaseAuth: FirebaseAuth,
) : BaseRepository() {

    fun getAuthReference() = firebaseAuth
}