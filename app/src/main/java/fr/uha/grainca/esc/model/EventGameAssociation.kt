package fr.uha.grainca.esc.model

import androidx.room.Entity
import androidx.room.Index

@Entity(tableName = "egas",
    primaryKeys = ["eid", "pid"],
    indices = [Index("eid"), Index("pid")]
)
class EventGameAssociation (
    val eid: Long,
    val pid: Long
)