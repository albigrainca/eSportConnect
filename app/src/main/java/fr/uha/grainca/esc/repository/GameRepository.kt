package fr.uha.grainca.esc.repository

import fr.uha.grainca.esc.database.GameDAO
import fr.uha.grainca.esc.model.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GameRepository (private val gameDAO: GameDAO) {
    fun getAll () : Flow<List<Game>> {
        return gameDAO.getAll()
    }

    fun getGameById (id : Long) : Flow<Game?> {
        return gameDAO.getGameById(id)
    }

    suspend fun create (game : Game) : Long = withContext(Dispatchers.IO) {
        return@withContext gameDAO.create(game)
    }

    suspend fun update (oldGame: Game, game: Game) : Long = withContext(Dispatchers.IO) {
        return@withContext gameDAO.update(game)
    }

    suspend fun upsert (game: Game) : Long = withContext(Dispatchers.IO) {
        return@withContext gameDAO.upsert(game)
    }

    suspend fun delete (game: Game) = withContext(Dispatchers.IO) {
        gameDAO.delete(game)
    }
}