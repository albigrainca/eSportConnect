package fr.uha.grainca.esc.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

class FullEvent (
    @Embedded
    val event: Event,

    @Relation(parentColumn = "mainGameId", entityColumn = "gid")
    val mainGame: Game?,

    @Relation(parentColumn = "eid", entityColumn = "gid", associateBy = Junction(EventGameAssociation::class))
    val otherGames: List<Game>,
)