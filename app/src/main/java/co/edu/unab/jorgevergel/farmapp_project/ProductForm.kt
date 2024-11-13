package co.edu.unab.jorgevergel.farmapp_project

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductForm(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }  // Controla la visibilidad del menú desplegable
    var imageUrl by remember { mutableStateOf("") }

    val categories = listOf(
        "Farmacia",
        "Maternidad y Bebés",
        "Dermocosmética",
        "Belleza",
        "Cuidado Personal",
        "Naturales",
        "Alimentos"
    )

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigate("home") }) {
                    Icon(Icons.Filled.Home, contentDescription = "Home", tint = Color(0xFF0D6EFD))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nuevo Producto", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D6EFD))
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                placeholder = { Text("Nombre del producto") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                placeholder = { Text("Descripción del producto") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Precio") },
                placeholder = { Text("Precio del producto") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Stock") },
                placeholder = { Text("Cantidad en stock") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Menú desplegable de categorías
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Categoría") },
                    readOnly = true,  // Evita que el usuario escriba directamente en el campo
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                category = item
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("Link de imagen") },
                placeholder = { Text("URL de la imagen del producto") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    saveProductToFirestore(
                        db = db,
                        name = name,
                        description = description,
                        price = price,
                        stock = stock,
                        category = category,
                        imageUrl = imageUrl,
                        onSuccess = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Producto guardado exitosamente")
                            }
                            // Limpiar formulario
                            name = ""
                            description = ""
                            price = ""
                            stock = ""
                            category = ""
                            imageUrl = ""
                        },
                        onFailure = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Error al guardar el producto")
                            }
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Producto", color = Color.White)
            }
        }
    }
}
fun saveProductToFirestore(
    db: FirebaseFirestore,
    name: String,
    description: String,
    price: String,
    stock: String,
    category: String,
    imageUrl: String,
    onSuccess: () -> Unit,
    onFailure: () -> Unit
) {
    val product = hashMapOf(
        "name" to name,
        "description" to description,
        "price" to price,
        "stock" to stock,
        "category" to category,
        "imageUrl" to imageUrl
    )

    db.collection("products")
        .add(product)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onFailure() }
}
