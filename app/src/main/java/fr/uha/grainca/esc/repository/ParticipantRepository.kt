package fr.uha.grainca.esc.repository

import fr.uha.grainca.esc.database.ParticipantDAO
import fr.uha.grainca.esc.model.Game
import fr.uha.grainca.esc.model.GameWithDetails
import fr.uha.grainca.esc.model.Participant
import fr.uha.grainca.esc.model.ParticipantWithDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ParticipantRepository (private val participantDAO: ParticipantDAO) {
    fun getAll () : Flow<List<ParticipantWithDetails>> {
        return participantDAO.getAllWithDetails()
    }

    fun getParticipantById (id : Long) : Flow<Participant?> {
        return participantDAO.getParticipantById(id)
    }

    suspend fun create (participant: Participant) : Long = withContext(Dispatchers.IO) {
        return@withContext participantDAO.create(participant)
    }

    suspend fun update (oldParticipant: Participant, participant: Participant) : Long = withContext(Dispatchers.IO) {
        return@withContext participantDAO.update(participant)
    }

    suspend fun upsert (participant: Participant) : Long = withContext(Dispatchers.IO) {
        return@withContext participantDAO.upsert(participant)
    }

    suspend fun delete (participant: Participant) = withContext(Dispatchers.IO) {
        participantDAO.delete(participant)
    }
}