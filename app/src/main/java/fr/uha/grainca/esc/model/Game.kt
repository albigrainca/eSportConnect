package fr.uha.grainca.esc.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity (tableName = "games")
data class Game(
    @PrimaryKey(autoGenerate = true)
    val gid: Long = 0,
    val name: String,
    val creator: String,
    val releaseDate: Date = Date(),
    val genre: Genre,
    val description: String,
    val picture : Uri?
){}