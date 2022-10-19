package com.yakupcan.nobetcieczane.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakupcan.nobetcieczane.common.Constants
import com.yakupcan.nobetcieczane.data.PushService
import com.yakupcan.nobetcieczane.data.Service
import com.yakupcan.nobetcieczane.data.repository.FirestoreRepositoryImpl
import com.yakupcan.nobetcieczane.data.repository.RepositoryImpl
import com.yakupcan.nobetcieczane.domain.repository.FirebaseFirestoreRepository
import com.yakupcan.nobetcieczane.domain.repository.PharmacyRepository
import com.yakupcan.nobetcieczane.domain.use_case.UseCases
import com.yakupcan.nobetcieczane.domain.use_case.firestore.FirebaseTokenGet
import com.yakupcan.nobetcieczane.domain.use_case.firestore.FirebaseTokenSave
import com.yakupcan.nobetcieczane.domain.use_case.firestore.SendPushAllDevicesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideServiceApi() : Service {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Service::class.java)
    }

    @Singleton
    @Provides
    fun providePharmacyRepository(api : Service) : PharmacyRepository {
        return RepositoryImpl(api)
    }

    @Provides
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    fun provideTokensRef(
        db : FirebaseFirestore
    ) = db.collection("tokens")

    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    fun provideFirebaseFirestoreRepository(
        booksRef: CollectionReference,
        pushAPI : PushService
    ): FirebaseFirestoreRepository = FirestoreRepositoryImpl(booksRef, pushAPI)

    @Provides
    fun provideUseCases(
        repo : FirebaseFirestoreRepository
    ) = UseCases(
        getToken = FirebaseTokenGet(repo),
        tokenSave = FirebaseTokenSave(repo),
        sendPushToAllDevicesUseCase = SendPushAllDevicesUseCase(repo)
    )

    @Provides
    fun providePushAPI() : PushService {
        return Retrofit.Builder()
            .baseUrl(Constants.FIREBASE_PUSH_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PushService::class.java)
    }
}