package fr.uha.grainca.esc.database

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.uha.grainca.esc.model.Game
import android.content.Context
import androidx.room.Room
import androidx.room.TypeConverters
import fr.uha.grainca.esc.model.Event
import fr.uha.grainca.esc.model.EventGameAssociation
import fr.uha.grainca.esc.model.EventParticipantAssociation
import fr.uha.grainca.esc.model.Participant
import fr.uha.grainca.esc.model.ParticipantGameAssociation
import fr.uha.hassenforder.android.database.DatabaseTypeConverters

@Database(
    entities = [
        Game::class,
        Participant::class,
        Event::class,
        EventGameAssociation::class,
        EventParticipantAssociation::class,
        ParticipantGameAssociation::class
    ],
    version=1,
    exportSchema = false
)
@TypeConverters(DatabaseTypeConverters::class)
abstract class ESportDatabase : RoomDatabase() {

    abstract val gameDAO : GameDAO
    abstract val eventDAO : EventDAO
    abstract val participantDAO: ParticipantDAO
    companion object {
        private lateinit var instance : ESportDatabase

        @Synchronized
        fun create (context : Context) : ESportDatabase {
            instance = Room.databaseBuilder(context, ESportDatabase::class.java, "ESport.db").build()
            return instance
        }

        @Synchronized
        fun get () : ESportDatabase {
            return instance
        }
    }
}