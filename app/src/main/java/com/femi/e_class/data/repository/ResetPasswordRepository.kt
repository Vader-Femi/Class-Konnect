package com.femi.e_class.data.repository

import com.google.firebase.auth.FirebaseAuth

interface ResetPasswordRepository {
    fun getAuthReference(): FirebaseAuth
}