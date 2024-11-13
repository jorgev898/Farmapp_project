package co.edu.unab.jorgevergel.farmapp_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.edu.unab.jorgevergel.farmapp_project.ui.theme.Farmapp_projectTheme

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Farmapp_projectTheme {
                val myController = rememberNavController()
                NavHost(
                    navController = myController,
                    startDestination = "login"
                ) {
                    composable("login") {
                        LoginScreen(myController)
                    }
                    composable("register") {
                        RegisterScreen(myController)
                    }

                    }
             }
            }
            }
        }

