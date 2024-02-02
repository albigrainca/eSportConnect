package fr.uha.grainca.esc.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import fr.uha.grainca.esc.model.Game
import fr.uha.grainca.esc.model.GameWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDAO {

    @Query("SELECT * FROM games")
    fun getAll () : Flow<List<Game>>

    @Query("SELECT * " +
            ", (SELECT COUNT(*) FROM events E WHERE E.mainGameId = G.gid) AS mainGameCount" +
            ", (SELECT COUNT(*) FROM egas EGA WHERE EGA.gid = G.gid) AS otherGameCount" +
            " FROM games AS G")
    fun getAllWithDetails () : Flow<List<GameWithDetails>>


    @Query("SELECT * FROM games WHERE gid = :id")
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