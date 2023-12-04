package fr.uha.grainca.esc.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import fr.uha.grainca.esc.model.Game
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDAO {

    @Query("SELECT * FROM games")
    fun getAll () : Flow<List<Game>>

    @Query("SELECT * FROM games WHERE pid = :id")
    fun getGameById (id : Long) : Flow<Game?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create (game : Game) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update (game : Game) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert (game : Game) : Long

    @Delete
    fun delete (game : Game)

}