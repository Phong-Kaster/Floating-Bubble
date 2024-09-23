package com.example.jetpack.injection.module

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private const val quoteDatabase = "QuoteDatabase"

//    @Provides
//    @Singleton
//    fun provideQuoteDatabase(@ApplicationContext context: Context): QuoteDatabase {
//        return Room.databaseBuilder(context = context, klass = QuoteDatabase::class.java, name = quoteDatabase)
//            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
//            .fallbackToDestructiveMigration()
//            .build()
//    }
}