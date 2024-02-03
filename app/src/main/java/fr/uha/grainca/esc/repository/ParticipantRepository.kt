package fr.uha.grainca.esc.repository

import androidx.annotation.WorkerThread
import fr.uha.grainca.esc.database.GameDAO
import fr.uha.grainca.esc.database.ParticipantDAO
import fr.uha.grainca.esc.model.Comparators
import fr.uha.grainca.esc.model.Event
import fr.uha.grainca.esc.model.EventGameAssociation
import fr.uha.grainca.esc.model.FullParticipant
import fr.uha.grainca.esc.model.Game
import fr.uha.grainca.esc.model.Participant
import fr.uha.grainca.esc.model.ParticipantGameAssociation
import fr.uha.grainca.esc.model.ParticipantWithDetails
import fr.uha.hassenforder.android.database.DeltaUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ParticipantRepository (
    private val participantDAO: ParticipantDAO,
    private val gameDao : GameDAO,

    ) {
    fun getAll () : Flow<List<ParticipantWithDetails>> {
        return participantDAO.getAllWithDetails()
    }

    fun getParticipantById (id : Long) : Flow<Participant?> {
        return participantDAO.getParticipantById(id)
    }

    fun getFullParticipantById (id : Long) : Flow<FullParticipant?> {
        return participantDAO.getFullParticipantById(id)
    }

    fun getGameById (id : Long) : Flow<Game?> {
        return gameDao.getGameById(id)
    }

    @WorkerThread
    suspend fun createParticipant(participant: Participant): Long = withContext(Dispatchers.IO) {
        return@withContext participantDAO.upsert(participant)
    }

    suspend fun update (oldParticipant: Participant, participant: Participant) : Long = withContext(Dispatchers.IO) {
        return@withContext participantDAO.update(participant)
    }

    suspend fun upsert (participant: Participant) : Long = withContext(Dispatchers.IO) {
        return@withContext participantDAO.upsert(participant)
    }

    @WorkerThread
    suspend fun saveParticipant(oldParticipant: FullParticipant, newParticipant: FullParticipant): Long = withContext(Dispatchers.IO) {
        var participantToSave : Participant? = null
        if (! Comparators.shallowEqualsParticipant(oldParticipant.participant, newParticipant.participant)) {
            participantToSave = newParticipant.participant
        }
        val participantId: Long = newParticipant.participant.pid
        val delta: DeltaUtil<Game, ParticipantGameAssociation> = object : DeltaUtil<Game, ParticipantGameAssociation>() {
            override fun getId(input: Game): Long {
                return input.gid
            }
            override fun same(initial: Game, now: Game): Boolean {
                return true
            }
            override fun createFor(input: Game): ParticipantGameAssociation {
                return ParticipantGameAssociation(participantId, input.gid)
            }
        }
        val oldList = oldParticipant.favoriteGames
        val newList = newParticipant.favoriteGames

        delta.calculate(oldList, newList)

        if (participantToSave != null) participantDAO.upsert(participantToSave)
        participantDAO.removeParticipantGames(delta.toRemove)
            participantDAO.addParticipantGames(delta.toAdd)

        return@withContext participantId
    }

    suspend fun delete (participant: Participant) = withContext(Dispatchers.IO) {
        participantDAO.delete(participant)
        participantDAO.deleteParticipantGame(participant.pid)
    }
}