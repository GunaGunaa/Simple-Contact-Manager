package com.example.jetpackcompose.di

import com.example.jetpackcompose.designs.Components
import com.example.jetpackcompose.network.CAServiceBuilder
import com.example.jetpackcompose.repository.CAContactRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Singleton
    @Provides
    fun getRetrofitInstance(): CAServiceBuilder {
        return CAServiceBuilder()
    }

    @Singleton
    @Provides
    fun getRepositoryInstance(serviceBuilder: CAServiceBuilder): CAContactRepository {
        return CAContactRepository(serviceBuilder)
    }

    @Singleton
    @Provides
    @Named("Guna")
    fun getAppName(): String {
        return "Guna"
    }

    @Singleton
    @Provides
    @Named("Guna1")
    fun getAppName1(): String {
        return "Guna1"
    }

    @Singleton
    @Provides
    fun getComponentsInstance():Components{
        return Components()
    }

}