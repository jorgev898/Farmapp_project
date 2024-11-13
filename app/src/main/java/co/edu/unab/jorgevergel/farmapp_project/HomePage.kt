package co.edu.unab.jorgevergel.farmapp_project

import androidx.compose.foundation.layout.*
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    navController: NavController,
    productViewModel: ProductViewModel = ProductViewModel(),
    onAddToCart: (CartItem) -> Unit,
    categories: List<String> = listOf(
        "Farmacia",
        "Maternidad y Bebés",
        "Dermocosmética",
        "Belleza",
        "Cuidado Personal",
        "Naturales",
        "Alimentos"
    ),
    categoryImages: Map<String, String> = mapOf(
        "Farmacia" to "https://cdn-icons-png.flaticon.com/128/3721/3721710.png",
        "Maternidad y Bebés" to "https://cdn-icons-png.flaticon.com/128/3274/3274199.png",
        "Dermocosmética" to "https://cdn-icons-png.flaticon.com/128/4337/4337668.png",
        "Belleza" to "https://cdn-icons-png.flaticon.com/128/3465/3465265.png",
        "Cuidado Personal" to "https://cdn-icons-png.flaticon.com/128/1754/1754656.png",
        "Naturales" to "https://cdn-icons-png.flaticon.com/128/4543/4543950.png",
        "Alimentos" to "https://cdn-icons-png.flaticon.com/128/3161/3161881.png"
    )
) {
    val products by productViewModel.getProducts().observeAsState(initial = emptyList())
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) } // Estado para el menú desplegable

    // Filtrar productos por búsqueda y categoría seleccionada
    val filteredProducts = products.filter {
        (it.name.contains(searchText, ignoreCase = true)) &&
                (selectedCategory.isEmpty() || it.category == selectedCategory)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FarmApp", color = Color.White) },
                actions = {
                    // Botón de carrito
                    IconButton(onClick = { navController.navigate("cart") }) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito", tint = Color.White)
                    }

                    // Botón de perfil con menú desplegable
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Perfil", tint = Color.White)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                navController.navigate("profile")
                                expanded = false
                            },
                            text = { Text("Mi Perfil") }
                        )
                        DropdownMenuItem(
                            onClick = {
                                // Cerrar sesión
                                FirebaseAuth.getInstance().signOut()  // Cierra sesión en Firebase
                                expanded = false
                                // Navegar a la pantalla de login
                                navController.navigate("login") {
                                    popUpTo("home") { inclusive = true } // Eliminar el historial de navegación
                                }
                            },
                            text = { Text("Log Out") }
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF0D6EFD))
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color(0xFFF8F9FA))
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Buscar productos...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Categorías", fontWeight = FontWeight.Bold, fontSize = 20.sp)

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    items(categories) { category ->
                        val imageUrl = categoryImages[category] ?: ""
                        CategoryItem(
                            name = category,
                            imageUrl = imageUrl,
                            isSelected = category == selectedCategory,
                            onClick = { selectedCategory = if (selectedCategory == category) "" else category }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Productos Destacados", fontWeight = FontWeight.Bold, fontSize = 20.sp)

                if (filteredProducts.isEmpty()) {
                    Text("No se encontraron productos.")
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 160.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(filteredProducts) { product ->
                            ProductCard(
                                product = product.name,
                                price = product.price,
                                imageUrl = product.imageUrl,
                                onAddToCart = {
                                    val newItem = CartItem(product.name, 1, product.price.toDouble())
                                    onAddToCart(newItem)
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Producto añadido correctamente")
                                    }
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )
}

@Composable
fun CategoryItem(name: String, imageUrl: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, // Alinea los elementos al centro
        verticalArrangement = Arrangement.Center, // Alinea verticalmente
        modifier = Modifier
            .width(100.dp)
            .height(120.dp) // Ajusta la altura para incluir el nombre debajo
            .clickable { onClick() }
    ) {
        Card(
            modifier = Modifier
                .size(80.dp), // Ajusta el tamaño del círculo
            colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFF0D6EFD) else Color.White),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if (imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = name,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(text = name.first().toString(), color = if (isSelected) Color.White else Color.Black, fontSize = 24.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp)) // Espacio entre el círculo y el nombre

        Text(
            text = name,
            color = if (isSelected) Color(0xFF0D6EFD) else Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            maxLines = 1, // Limita a una sola línea
            overflow = TextOverflow.Ellipsis, // Añade "..." si el texto no cabe
            modifier = Modifier.padding(horizontal = 4.dp) // Ajuste de padding horizontal
        )
    }
}

@Composable
fun ProductCard(product: String, price: String, imageUrl: String, onAddToCart: () -> Unit) {
    // Formateo del precio a formato de pesos colombianos
    val formattedPrice = try {
        val parsedPrice = price.toDouble()
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
        numberFormat.format(parsedPrice)
    } catch (e: Exception) {
        price // En caso de que el precio no sea válido, se muestra tal cual
    }
    Card(
        modifier = Modifier
            .width(160.dp)
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = imageUrl,
                contentDescription = product,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.White)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1, // Limita a una sola línea
                overflow = TextOverflow.Ellipsis, // Añade "..." si el texto no cabe
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = formattedPrice,
                color = Color(0xFF0D6EFD),
                fontWeight = FontWeight.Bold,
                maxLines = 1, // Limita a una sola línea
                overflow = TextOverflow.Ellipsis, // Añade "..." si el texto no cabe
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = onAddToCart,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD)) // Usa containerColor en lugar de backgroundColor
            ) {
                Text("Añadir al carrito")
            }
        }
    }
}