package fr.uha.grainca.esc.model

import androidx.room.Entity
import androidx.room.Index

@Entity(tableName = "pgas",
    primaryKeys = ["pid", "gid"],
    indices = [Index("pid"), Index("gid")]
)
class ParticipantGameAssociation (
    val pid: Long,
    val gid: Long
)