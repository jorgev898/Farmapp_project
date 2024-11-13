package co.edu.unab.jorgevergel.farmapp_project


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun EditProductForm(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var productId by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var imageUrl by remember { mutableStateOf("") }
    var isProductFound by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var productAction by remember { mutableStateOf("") }

    val categories = listOf(
        "Farmacia", "Maternidad y Bebés", "Dermocosmética", "Belleza",
        "Cuidado Personal", "Naturales", "Alimentos"
    )

    // Función para buscar producto por ID o por nombre
    fun fetchProduct() {
        if (productId.isNotEmpty()) {
            db.collection("products").document(productId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        name = document.getString("name") ?: ""
                        description = document.getString("description") ?: ""
                        price = document.getString("price") ?: ""
                        stock = document.getString("stock") ?: ""
                        category = document.getString("category") ?: ""
                        imageUrl = document.getString("imageUrl") ?: ""
                        isProductFound = true
                        showDialog = true // Mostrar las opciones de acción
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Producto no encontrado")
                        }
                    }
                }
        } else if (name.isNotEmpty()) { // Buscar por nombre si el ID está vacío
            db.collection("products")
                .whereEqualTo("name", name)
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {
                        val document = result.documents.first() // Obtener el primer documento encontrado
                        productId = document.id // Asignar el ID del producto encontrado
                        description = document.getString("description") ?: ""
                        price = document.getString("price") ?: ""
                        stock = document.getString("stock") ?: ""
                        category = document.getString("category") ?: ""
                        imageUrl = document.getString("imageUrl") ?: ""
                        isProductFound = true
                        showDialog = true // Mostrar las opciones de acción
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Producto no encontrado")
                        }
                    }
                }
        }
    }

    // Función para limpiar el formulario
    fun clearForm() {
        productId = ""
        name = ""
        description = ""
        price = ""
        stock = ""
        category = ""
        imageUrl = ""
        isProductFound = false
    }

    // Función para actualizar producto en Firebase
    fun updateProduct() {
        if (productId.isNotEmpty()) {
            val productUpdates = hashMapOf<String, Any>(
                "name" to name,
                "description" to description,
                "price" to price,
                "stock" to stock,
                "category" to category,
                "imageUrl" to imageUrl
            )

            db.collection("products").document(productId)
                .update(productUpdates)
                .addOnSuccessListener {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Producto modificado")
                    }
                    // Limpiar el formulario después de la actualización
                    clearForm()
                }
                .addOnFailureListener {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Error al modificar el producto")
                    }
                }
        }
    }

    // Función para eliminar producto de Firebase
    fun deleteProduct() {
        if (productId.isNotEmpty()) {
            db.collection("products").document(productId)
                .delete()
                .addOnSuccessListener {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Producto eliminado")
                    }
                    clearForm() // Limpiar el formulario después de la eliminación
                    navController.popBackStack() // Volver a la pantalla anterior
                }
                .addOnFailureListener {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Error al eliminar el producto")
                    }
                }
        }
    }

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
                Text("Editar Producto", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D6EFD))
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        // Contenedor desplazable
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),  // Habilitar el desplazamiento
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = productId,
                onValueChange = { productId = it },
                label = { Text("ID del Producto (opcional)") },
                placeholder = { Text("Ingrese el ID del producto") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del Producto") },
                placeholder = { Text("Ingrese el nombre del producto") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { fetchProduct() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Buscar Producto", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar opciones de editar o eliminar si el producto es encontrado
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Selecciona una acción") },
                    text = {
                        Column {
                            Button(
                                onClick = {
                                    productAction = "edit"
                                    showDialog = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD)) // Azul
                            ) {
                                Text("Editar Producto", color = Color.White)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    productAction = "delete"
                                    deleteProduct() // Eliminar producto directamente
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD)) // Azul
                            ) {
                                Text("Eliminar Producto", color = Color.White)
                            }
                        }
                    },
                    confirmButton = {},
                    dismissButton = {}
                )
            }

            // Mostrar formulario para editar el producto si se selecciona "editar"
            if (isProductFound && productAction == "edit") {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Editar Nombre del Producto") },
                    placeholder = { Text("Modifique el nombre del producto") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text("Stock") },
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
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()  // Usamos el modificador original
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },  // Añadido el parámetro 'text'
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
                    label = { Text("URL de la Imagen") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { updateProduct() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar Producto", color = Color.White)
                }
            }
        }
    }
}