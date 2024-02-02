package fr.uha.grainca.esc.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "participants")
data class Participant (
    @PrimaryKey(autoGenerate = true)
    val pid: Long = 0,
    val gamerName: String,
    val realName: String,
    val age: Int,
    val level: GamerLevel
){}