package fr.uha.grainca.esc.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity (tableName = "games")
data class Game(
    @PrimaryKey(autoGenerate = true)
    val pid: Long = 0,
    val name: String,
    val creator: String,
    val releaseDate: Date,
    val genre: Genre,
    val description: String
){}