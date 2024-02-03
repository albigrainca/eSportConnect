package fr.uha.grainca.esc.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import fr.uha.grainca.esc.model.Event
import fr.uha.grainca.esc.model.EventGameAssociation
import fr.uha.grainca.esc.model.EventParticipantAssociation
import fr.uha.grainca.esc.model.FullEvent
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDAO {
    @Query("SELECT * FROM events")
    fun getAll () : Flow<List<Event>>   

    @Query("SELECT * FROM events WHERE eid = :id")
    @Transaction
    fun getEventById (id : Long) : Flow<FullEvent?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create (event : Event) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update (event : Event) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert (event : Event) : Long

    @Delete
    fun delete (event : Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEventGame(otherGame: EventGameAssociation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEventParticipant(guest: EventParticipantAssociation)

    @Delete
    suspend fun removeEventGame(otherGame: EventGameAssociation)

    @Delete
    suspend fun removeEventParticipant(guest: EventParticipantAssociation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEventGame(othersGames: List<EventGameAssociation>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEventParticipant(guests: List<EventParticipantAssociation>)

    @Delete
    suspend fun removeEventGame(othersGames: List<EventGameAssociation>)

    @Delete
    suspend fun removeEventParticipant(guest: List<EventParticipantAssociation>)

    @Query ("DELETE FROM egas WHERE eid = :eid")
    fun deleteEventGame(eid: Long)

    @Query ("DELETE FROM epas WHERE eid = :eid")
    fun deleteEventParticipant(eid: Long)
}
