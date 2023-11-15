package fr.uha.grainca.esc.repository

import fr.uha.grainca.esc.database.GameDAO
import fr.uha.grainca.esc.model.Game
import kotlinx.coroutines.flow.Flow

class GameRepository (private val gameDAO: GameDAO) {
    fun getAll () : Flow<List<Game>> {
        return gameDAO.getAll()
    }

    fun getGameById (id : Long) : Flow<Game?> {
        return gameDAO.getGameById(id)
    }

    fun create (game : Game) : Long {
        return gameDAO.create(game)
    }

    fun update (game: Game) : Long {
        return gameDAO.update(game)
    }

    fun upsert (game: Game) : Long {
        return gameDAO.upsert(game)
    }

    fun delete (game: Game) {
        return gameDAO.delete(game)
    }
}