package fr.uha.grainca.esc.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import fr.uha.grainca.esc.model.EventGameAssociation
import fr.uha.grainca.esc.model.EventParticipantAssociation
import fr.uha.grainca.esc.model.FullParticipant
import fr.uha.grainca.esc.model.Participant
import fr.uha.grainca.esc.model.ParticipantGameAssociation
import fr.uha.grainca.esc.model.ParticipantWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface ParticipantDAO {
    @Query("SELECT * FROM participants")
    fun getAll () : Flow<List<Participant>>

    @Query("SELECT *, (SELECT COUNT(*) FROM epas EPA WHERE EPA.pid = P.pid) AS guestCount " +
            "FROM participants AS P")
    fun getAllWithDetails(): Flow<List<ParticipantWithDetails>>

    @Query("SELECT * FROM participants")
    @Transaction
    fun getAllFullParticipants(): Flow<List<FullParticipant>>

    @Query("SELECT * FROM participants WHERE pid = :id")
    fun getParticipantById (id : Long) : Flow<Participant?>

    @Query("SELECT * FROM participants WHERE pid = :id")
    @Transaction
    fun getFullParticipantById (id : Long) : Flow<FullParticipant?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create (participant: Participant) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update (participant: Participant) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert (participant: Participant) : Long

    @Delete
    fun delete (participant: Participant)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addParticipantGame(favoriteGame: ParticipantGameAssociation)

    @Delete
    suspend fun removeParticipantGame(favoriteGame: ParticipantGameAssociation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addParticipantGames(favoriteGames: List<ParticipantGameAssociation>)

    @Delete
    suspend fun removeParticipantGames(favoriteGames: List<ParticipantGameAssociation>)

    @Query ("DELETE FROM pgas WHERE pid = :pid")
    fun deleteParticipantGame(pid: Long)

}