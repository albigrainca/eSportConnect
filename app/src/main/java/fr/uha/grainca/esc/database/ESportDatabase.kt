package fr.uha.grainca.esc.database

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.uha.grainca.esc.model.Game
import android.content.Context
import androidx.room.Room

@Database(
    entities = [
        Game::class,
    ],
    version=1,
    exportSchema = false
)
abstract class ESportDatabase : RoomDatabase() {
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