package fr.uha.grainca.esc.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

class FullParticipant (
    @Embedded
    val participant: Participant,

    @Relation(parentColumn = "pid", entityColumn = "gid", associateBy = Junction(ParticipantGameAssociation::class))
    val favoriteGames: List<Game>,
)