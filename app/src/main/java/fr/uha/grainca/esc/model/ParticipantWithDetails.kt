package fr.uha.grainca.esc.model

import androidx.room.Embedded

class ParticipantWithDetails (
    @Embedded
    val participant: Participant,

    val guestCount: Int,
)