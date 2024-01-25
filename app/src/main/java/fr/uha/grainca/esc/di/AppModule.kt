package fr.uha.grainca.esc.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.uha.grainca.esc.database.ESportDatabase
import fr.uha.grainca.esc.database.EventDAO
import fr.uha.grainca.esc.database.GameDAO
import fr.uha.grainca.esc.repository.EventRepository
import fr.uha.grainca.esc.repository.GameRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    @Singleton
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = ESportDatabase.create(appContext)

    @Singleton
    @Provides
    fun provideEventDao(db: ESportDatabase) = db.eventDAO

    @Singleton
    @Provides
    fun provideGameDao(db: ESportDatabase) = db.gameDAO

    @Singleton
    @Provides
    fun provideGameRepository(
//        dispatcher: CoroutineDispatcher,
        gameDao: GameDAO
    ) = GameRepository(gameDao)

    @Singleton
    @Provides
    fun provideEventRepository(
//        dispatcher: CoroutineDispatcher,
        eventDao: EventDAO
    ) = EventRepository(eventDao)
}