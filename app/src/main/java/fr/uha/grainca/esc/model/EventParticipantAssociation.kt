package fr.uha.grainca.esc.model

import androidx.room.Entity
import androidx.room.Index

@Entity(tableName = "epas",
    primaryKeys = ["eid", "pid"],
    indices = [Index("eid"), Index("pid")]
)
class EventParticipantAssociation (
    val eid: Long,
    val pid: Long
)