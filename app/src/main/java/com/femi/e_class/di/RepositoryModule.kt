package com.femi.e_class.di

import com.femi.e_class.data.repository.*
import com.femi.e_class.data.repository.authentication.AuthenticationRepository
import com.femi.e_class.data.repository.authentication.AuthenticationRepositoryImpl
import com.femi.e_class.data.repository.base.BaseRepository
import com.femi.e_class.data.repository.base.BaseRepositoryImpl
import com.femi.e_class.data.repository.main_activity.MainActivityRepository
import com.femi.e_class.data.repository.main_activity.MainActivityRepositoryImpl
import com.femi.e_class.data.repository.user_activity.UserActivityRepository
import com.femi.e_class.data.repository.user_activity.UserActivityRepositoryImpl
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
    abstract fun bindAuthenticationRepository(
        authenticationRepositoryImpl: AuthenticationRepositoryImpl
    ): AuthenticationRepository

    @Binds
    @Singleton
    abstract fun bindUserActivityRepository(
        userActivityRepositoryImpl: UserActivityRepositoryImpl
    ): UserActivityRepository
}