package com.femi.e_class.di

import android.app.Application
import com.femi.e_class.data.UserPreferences
import com.femi.e_class.domain.use_case.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStore(application: Application): UserPreferences{
        return UserPreferences(application)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthReference(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseCollectionReference(): CollectionReference{
        return FirebaseFirestore.getInstance().collection("Users")
    }

    @Provides
    @Singleton
    fun provideValidateEmail(application: Application): ValidateEmail{
        return ValidateEmail(application)
    }

    @Provides
    @Singleton
    fun provideValidateLogInPassword(application: Application): ValidateLogInPassword {
        return ValidateLogInPassword(application)
    }
    @Provides
    @Singleton
    fun provideValidateName(application: Application): ValidateName{
        return ValidateName(application)
    }

    @Provides
    @Singleton
    fun provideValidateMatric(application: Application): ValidateMatricOrId{
        return ValidateMatricOrId(application)
    }

    @Provides
    @Singleton
    fun provideValidateSignUpPassword(application: Application): ValidateSignUpPassword {
        return ValidateSignUpPassword(application)
    }
    @Provides
    @Singleton
    fun provideValidateCourseCode(application: Application): ValidateCourseCode{
        return ValidateCourseCode(application)
    }

}