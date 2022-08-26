package com.android.nobetcieczane.di

import com.android.nobetcieczane.data.Service
import com.android.nobetcieczane.data.repository.RepositoryImpl
import com.android.nobetcieczane.domain.repository.PharmacyRepository
import com.android.nobetcieczane.common.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideServiceApi(): Service {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Service::class.java)
    }

    @Singleton
    @Provides
    fun providePharmacyRepository(api: Service): PharmacyRepository {
        return RepositoryImpl(api)
    }
}