package com.femi.e_class.di

import com.femi.e_class.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLogInRepository(
        logInRepositoryImpl: LogInRepositoryImpl
    ): LogInRepository


    @Binds
    @Singleton
    abstract fun bindBaseRepository(
        baseRepositoryImpl: BaseRepositoryImpl
    ): BaseRepository

    @Binds
    @Singleton
    abstract fun bindMainActivityRepository(
        baseRepositoryImpl: MainActivityRepositoryImpl
    ): MainActivityRepository


    @Binds
    @Singleton
    abstract fun bindResetPasswordRepository(
        resetPasswordRepositoryImpl: ResetPasswordRepositoryImpl
    ): ResetPasswordRepository

    @Binds
    @Singleton
    abstract fun bindSignUpRepository(
        signUpRepositoryImpl: SignUpRepositoryImpl
    ): SignUpRepository

    @Binds
    @Singleton
    abstract fun bindHomeActivityRepository(
        homeActivityRepositoryImpl: HomeActivityRepositoryImpl
    ): HomeActivityRepository
}