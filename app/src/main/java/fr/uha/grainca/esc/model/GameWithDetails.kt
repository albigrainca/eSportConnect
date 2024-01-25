package fr.uha.grainca.esc.model

import androidx.room.Embedded

class GameWithDetails (
    @Embedded
    val game: Game,

    val mainGameCount : Int,
    val otherGameCount: Int,
)