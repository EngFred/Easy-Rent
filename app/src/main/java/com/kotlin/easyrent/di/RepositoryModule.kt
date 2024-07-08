package com.kotlin.easyrent.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kotlin.easyrent.features.auth.data.repository.AuthRepositoryImpl
import com.kotlin.easyrent.features.auth.domain.repository.AuthRepository
import com.kotlin.easyrent.features.auth.domain.usecase.ValidateConfirmPasswordUseCase
import com.kotlin.easyrent.features.auth.domain.usecase.ValidateEmailUseCase
import com.kotlin.easyrent.features.auth.domain.usecase.ValidateNameUseCase
import com.kotlin.easyrent.features.auth.domain.usecase.ValidatePasswordUseCase
import com.kotlin.easyrent.features.auth.domain.usecase.ValidatePhoneNumberUseCase
import com.kotlin.easyrent.features.profile.data.repository.ProfileRepositoryImpl
import com.kotlin.easyrent.features.profile.domain.repository.ProfileRepository
import com.kotlin.easyrent.features.rentalManagement.data.cache.CacheDatabase
import com.kotlin.easyrent.features.rentalManagement.data.repository.RentalsRepositoryImpl
import com.kotlin.easyrent.features.rentalManagement.domain.repository.RentalsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun providesProfileRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ): ProfileRepository = ProfileRepositoryImpl(firebaseAuth, firestore, firebaseStorage)


    @Provides
    @Singleton
    fun providesRentalsRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage,
        cacheDatabase: CacheDatabase,
        @ApplicationContext context: Context
    ) : RentalsRepository = RentalsRepositoryImpl(
        firebaseAuth,
        firestore,
        firebaseStorage,
        cacheDatabase,
        context
    )

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