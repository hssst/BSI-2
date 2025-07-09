package com.maral.happyplacesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.maral.happyplacesapp.ui.navigation.NavGraph
import com.maral.happyplacesapp.ui.theme.HappyPlacesAppTheme


/**
 * Main entry point of the app.
 * Sets up Jetpack Compose and shows the navigation graph.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HappyPlacesAppTheme {
                Surface {
                    HappyPlacesApp()
                }
            }
        }
    }
}

/**
 * Root composable that holds the navigation controller and graph.
 */
@Composable
fun HappyPlacesApp() {
    val navController = rememberNavController()
    NavGraph(navController = navController)
}

/**
 * Preview for the app, useful for testing in Android Studio.
 */
@Preview(showBackground = true)
@Composable
fun AppPreview() {
    HappyPlacesAppTheme {
        HappyPlacesApp()
    }
}


