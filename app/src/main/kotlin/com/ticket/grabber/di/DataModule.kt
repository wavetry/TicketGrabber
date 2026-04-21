package com.ticket.grabber.di

import android.content.Context
import com.ticket.grabber.data.api.TicketApi
import com.ticket.grabber.data.local.SearchHistoryDao
import com.ticket.grabber.data.local.AuthDao
import com.ticket.grabber.data.local.AuthSessionDatabase
import com.ticket.grabber.data.repository.*
import com.ticket.grabber.data.repository.remote.*
import com.ticket.grabber.data.api.*
import com.ticket.grabber.data.api.auth.AuthInterceptor
import com.ticket.grabber.data.api.auth.AuthRepository
import com.ticket.grabber.data.api.auth.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * 数据层依赖注入模块
 * 提供所有数据层相关的依赖
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    
    @Provides
    @Singleton
    fun provideTicketApi(
        okHttpClient: OkHttpClient
    ): TicketApi {
        return Retrofit.Builder()
            .baseUrl("https://api.12306.cn/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TicketApi::class.java)
    }
    
    @Provides
    @Singleton
    fun provideTicketDatabase(@ApplicationContext context: Context): TicketDatabase {
        return Room.databaseBuilder(
            context,
            TicketDatabase::class.java,
            "ticket_grabber.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    
    @Provides
    fun provideStationDao(database: TicketDatabase): StationDao {
        return database.stationDao()
    }
    
    @Provides
    fun provideTrainDao(database: TicketDatabase): TrainDao {
        return database.trainDao()
    }
    
    @Provides
    fun providePassengerDao(database: TicketDatabase): PassengerDao {
        return database.passengerDao()
    }
    
    @Provides
    fun provideOrderDao(database: TicketDatabase): OrderDao {
        return database.orderDao()
    }
    
    @Provides
    fun providePaymentDao(database: TicketDatabase): PaymentDao {
        return database.paymentDao()
    }
    
    @Provides
    fun provideSearchHistoryDao(database: TicketDatabase): SearchHistoryDao {
        return database.searchHistoryDao()
    }
    
    @Provides
    fun provideGrabTaskDao(database: TicketDatabase): GrabTaskDao {
        return database.grabTaskDao()
    }
    
    @Provides
    fun provideGrabResultDao(database: TicketDatabase): GrabResultDao {
        return database.grabResultDao()
    }
    
    // ==================== Auth Database ====================
    
    @Provides
    @Singleton
    fun provideAuthSessionDatabase(@ApplicationContext context: Context): AuthSessionDatabase {
        return Room.databaseBuilder(
            context,
            AuthSessionDatabase::class.java,
            "auth_session.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    
    @Provides
    fun provideAuthDao(database: AuthSessionDatabase): AuthDao {
        return database.authDao()
    }
    
    // ==================== Auth Repository ====================
    
    @Provides
    @Singleton
    fun provideAuthRepository(
        api: TicketApi,
        authDao: AuthDao
    ): AuthRepository {
        return AuthRepositoryImpl(api, authDao)
    }
    
    @Provides
    @Singleton
    fun provideAuthInterceptor(
        authRepository: AuthRepository
    ): AuthInterceptor {
        return AuthInterceptor(authRepository)
    }
    
    // ==================== Repositories ====================
    
    @Provides
    @Singleton
    fun provideStationRepository(
        api: TicketApi,
        stationDao: StationDao
    ): StationRepository {
        return RemoteStationRepository(api, stationDao)
    }
    
    @Provides
    @Singleton
    fun provideTrainRepository(
        api: TicketApi,
        trainDao: TrainDao
    ): TrainRepository {
        return RemoteTrainRepository(api, trainDao)
    }
    
    @Provides
    @Singleton
    fun providePassengerRepository(
        api: TicketApi,
        passengerDao: PassengerDao
    ): PassengerRepository {
        return RemotePassengerRepository(api, passengerDao)
    }
    
    @Provides
    @Singleton
    fun provideOrderRepository(
        api: TicketApi,
        orderDao: OrderDao
    ): OrderRepository {
        return RemoteOrderRepository(api, orderDao)
    }
    
    @Provides
    @Singleton
    fun providePaymentRepository(
        api: TicketApi,
        paymentDao: PaymentDao
    ): PaymentRepository {
        return RemotePaymentRepository(api, paymentDao)
    }
    
    @Provides
    @Singleton
    fun provideTicketRepository(
        api: TicketApi
    ): TicketRepository {
        return TicketRepositoryImpl(api)
    }
    
    @Provides
    @Singleton
    fun provideGrabTaskRepository(
        api: TicketApi,
        taskDao: GrabTaskDao,
        resultDao: GrabResultDao,
        ticketRepository: TicketRepository
    ): GrabTaskRepository {
        return GrabTaskRepositoryImpl(api, taskDao, resultDao, ticketRepository)
    }
}
