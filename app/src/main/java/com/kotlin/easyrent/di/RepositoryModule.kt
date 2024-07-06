package com.kotlin.easyrent.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kotlin.easyrent.features.auth.data.repository.AuthRepositoryImpl
import com.kotlin.easyrent.features.auth.domain.repository.AuthRepository
import com.kotlin.easyrent.features.auth.domain.usecase.ValidateConfirmPasswordUseCase
import com.kotlin.easyrent.features.auth.domain.usecase.ValidateEmailUseCase
import com.kotlin.easyrent.features.auth.domain.usecase.ValidateNameUseCase
import com.kotlin.easyrent.features.auth.domain.usecase.ValidatePasswordUseCase
import com.kotlin.easyrent.features.auth.domain.usecase.ValidatePhoneNumberUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesAuthRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthRepository = AuthRepositoryImpl(firebaseAuth, firestore)

    @Provides
    @Singleton
    fun providesValidatePasswordUseCase() = ValidatePasswordUseCase()

    @Provides
    @Singleton
    fun providesValidateEmailUseCase() = ValidateEmailUseCase()

    @Provides
    @Singleton
    fun providesValidateConfirmPasswordUseCase() = ValidateConfirmPasswordUseCase()


    @Provides
    @Singleton
    fun providesValidateNameUseCase() = ValidateNameUseCase()

    @Provides
    @Singleton
    fun providesValidatePhoneNumberUseCase() = ValidatePhoneNumberUseCase()


}