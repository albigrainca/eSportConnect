package fr.uha.grainca.esc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.uha.grainca.esc.database.ESportDatabase
import fr.uha.grainca.esc.ui.game.CreateGameScreen
import fr.uha.grainca.esc.ui.game.ListGamesScreen
import fr.uha.grainca.esc.ui.theme.ESportConnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ESportDatabase.create(applicationContext)
        setContent {
            ESportConnectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ListGamesScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ESportConnectTheme {
        Greeting("Android")
    }
}