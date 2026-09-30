package com.alagamb.petcompose.di

import com.alagamb.petcompose.repo.pet.PetRepository
import com.alagamb.petcompose.repo.pet.PetRepositoryImpl
import com.alagamb.petcompose.repo.request.RequestRepository
import com.alagamb.petcompose.repo.request.RequestRepositoryImpl
import com.alagamb.petcompose.repo.user.UserRepository
import com.alagamb.petcompose.repo.user.UserRepositoryImpl
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
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindPetRepository(petRepositoryImpl: PetRepositoryImpl): PetRepository

    @Binds
    @Singleton
    abstract fun bindRequestRepository(requestRepositoryImpl: RequestRepositoryImpl): RequestRepository
}
