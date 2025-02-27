package ch.js.tagalarm.dagger

import android.content.Context
import androidx.room.Room
import ch.js.tagalarm.data.db.AlarmDatabase
import ch.js.tagalarm.data.db.AlarmRepository
import ch.js.tagalarm.data.db.AlarmRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAlarmDatabase(@ApplicationContext context: Context): AlarmDatabase =
        Room.databaseBuilder(
            context,
            AlarmDatabase::class.java,
            "alarm_database",
        )
            .build()

    @Provides
    @Singleton
    fun provideAlarmRepository(database: AlarmDatabase): AlarmRepository = AlarmRepositoryImpl(database.alarmDao(), database.nfcTagDao())
}
