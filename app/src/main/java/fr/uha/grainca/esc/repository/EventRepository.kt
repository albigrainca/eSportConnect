package fr.uha.grainca.esc.repository

import fr.uha.grainca.esc.database.EventDAO
import fr.uha.grainca.esc.model.Event
import fr.uha.grainca.esc.model.FullEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class EventRepository (private val eventDao : EventDAO)
{
    fun getAll () : Flow<List<Event>> {
        return eventDao.getAll()
    }

    fun getEventById (id : Long) : Flow<FullEvent?> {
        return eventDao.getEventById(id)
    }

    suspend fun create (event : Event) : Long = withContext(Dispatchers.IO) {
        return@withContext eventDao.create(event)
    }

    suspend fun update (oldEvent : Event, event : Event) : Long = withContext(Dispatchers.IO) {
        return@withContext eventDao.update(event)
    }

    suspend fun upsert (event : Event) : Long = withContext(Dispatchers.IO) {
        return@withContext eventDao.upsert(event)
    }

    suspend fun delete (event : Event) = withContext(Dispatchers.IO) {
        eventDao.delete(event)
    }
}