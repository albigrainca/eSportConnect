package fr.uha.grainca.esc.repository

import androidx.annotation.WorkerThread
import fr.uha.grainca.esc.database.EventDAO
import fr.uha.grainca.esc.database.GameDAO
import fr.uha.grainca.esc.model.Comparators
import fr.uha.grainca.esc.model.Event
import fr.uha.grainca.esc.model.EventGameAssociation
import fr.uha.grainca.esc.model.FullEvent
import fr.uha.grainca.esc.model.Game
import fr.uha.hassenforder.android.database.DeltaUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class EventRepository (
    private val eventDao : EventDAO,
    private val gameDao : GameDAO
)
{
    fun getAll () : Flow<List<Event>> {
        return eventDao.getAll()
    }

    fun getEventById (id : Long) : Flow<FullEvent?> {
        return eventDao.getEventById(id)
    }

    fun getGameById (id : Long) : Flow<Game?> {
        return gameDao.getGameById(id)
    }

    @WorkerThread
    suspend fun createEvent(event: Event): Long = withContext(Dispatchers.IO) {
        return@withContext eventDao.upsert(event)
    }

    @WorkerThread
    suspend fun saveEvent(oldEvent: FullEvent, newEvent: FullEvent): Long = withContext(Dispatchers.IO) {
        var eventToSave : Event? = null
        if (! Comparators.shallowEqualsEvent(oldEvent.event, newEvent.event)) {
            eventToSave = newEvent.event
        }
        val eventId: Long = newEvent.event.eid
        val delta: DeltaUtil<Game, EventGameAssociation> = object : DeltaUtil<Game, EventGameAssociation>() {
            override fun getId(input: Game): Long {
                return input.gid
            }
            override fun same(initial: Game, now: Game): Boolean {
                return true
            }
            override fun createFor(input: Game): EventGameAssociation {
                return EventGameAssociation(eventId, input.gid)
            }
        }
        val oldList = oldEvent.otherGames
        val newList = newEvent.otherGames
        delta.calculate(oldList, newList)

        if (eventToSave != null) eventDao.upsert(eventToSave)
        eventDao.removeEventGame(delta.toRemove)
        eventDao.addEventGame(delta.toAdd)

        return@withContext eventId
    }

    suspend fun delete(event: Event) = withContext(Dispatchers.IO) {
        eventDao.delete(event)
        eventDao.deleteEventGame (event.eid)
    }
}