package fr.uha.grainca.esc.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "events")
data class Event (
    @PrimaryKey(autoGenerate = true)
    val eid: Long = 0,
    val name: String,
    val startDay: Date = Date(),
    val duration: Int = 0,
    val mainGameId: Long = 0,
)