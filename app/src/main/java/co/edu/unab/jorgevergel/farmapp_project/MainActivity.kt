package co.edu.unab.jorgevergel.farmapp_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            // Lista de productos en el carrito
            var cartItems by remember { mutableStateOf(listOf<CartItem>()) }

            // Usar Scaffold para la barra de navegación
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(
                            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                            label = { Text("Home") },
                            selected = currentRoute == "home",
                            onClick = {
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Filled.Add, contentDescription = "Add Product") },
                            label = { Text("Add Product") },
                            selected = currentRoute == "productForm",
                            onClick = {
                                navController.navigate("productForm") {
                                    popUpTo("home")
                                    launchSingleTop = true
                                }
                            }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Filled.Edit, contentDescription = "Edit Product") },
                            label = { Text("Edit Product") },
                            selected = currentRoute == "editProduct",
                            onClick = {
                                navController.navigate("editProduct") {
                                    popUpTo("home") { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        )

                    }
                }
            ) { paddingValues ->
                // Definir las rutas para cada pantalla
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.padding(paddingValues)
                ) {
                    composable("home") {
                        HomePage(
                            navController = navController,
                            onAddToCart = { item ->
                                // Lógica para agrupar productos por nombre e incrementar cantidad si ya existe en el carrito
                                val existingItem = cartItems.find { it.name == item.name }
                                if (existingItem != null) {
                                    existingItem.quantity++
                                    cartItems = cartItems.toList() // Notifica el cambio
                                } else {
                                    cartItems = cartItems + item
                                }
                            }
                        )
                    }
                    composable("productForm") { ProductForm(navController) }
                    composable("editProduct") { EditProductForm(navController)}
                    composable("cart") { CartScreen(cartItems = cartItems) }
                    composable("profile") { ProfileScreen() }
                    }
                }
            }
        }
    }

