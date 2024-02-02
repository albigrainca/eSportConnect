package fr.uha.grainca.esc.model

import androidx.room.Entity
import androidx.room.Index

@Entity(tableName = "egas",
    primaryKeys = ["eid", "gid"],
    indices = [Index("eid"), Index("gid")]
)
class EventGameAssociation (
    val eid: Long,
    val gid: Long
)