package com.example.mob21project.core.di

import com.example.mob21project.data.repo.ActivitiesRepo
import com.example.mob21project.data.repo.ActivitiesRepoFireImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideAuthService(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    @Provides
    @Singleton
    fun provideRepo(): ActivitiesRepo {
        return ActivitiesRepoFireImpl()
    }
}