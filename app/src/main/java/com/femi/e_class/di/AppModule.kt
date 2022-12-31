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
    fun provideValidateEmail(appContext: Application): ValidateEmail{
        return ValidateEmail(appContext)
    }

    @Provides
    @Singleton
    fun provideValidateLogInPassword(appContext: Application): ValidateLogInPassword {
        return ValidateLogInPassword(appContext)
    }
    @Provides
    @Singleton
    fun provideValidateName(appContext: Application): ValidateName{
        return ValidateName(appContext)
    }

    @Provides
    @Singleton
    fun provideValidateMatric(appContext: Application): ValidateMatric{
        return ValidateMatric(appContext)
    }

    @Provides
    @Singleton
    fun provideValidateSignUpPassword(appContext: Application): ValidateSignUpPassword {
        return ValidateSignUpPassword(appContext)
    }
    @Provides
    @Singleton
    fun provideValidateCourseCode(appContext: Application): ValidateCourseCode{
        return ValidateCourseCode(appContext)
    }

}