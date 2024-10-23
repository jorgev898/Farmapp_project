package co.edu.unab.jorgevergel.farmapp_project
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.engage.shopping.datamodel.ShoppingCart

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomePage() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "FarmApp",
                        color = Color.White
                    )
                },
                actions = {
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = "Carrito",
                            tint = Color.White
                        )
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF0D6EFD)
                        ),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Iniciar Sesión")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF0D6EFD)
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color(0xFFF8F9FA))
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Campo de búsqueda
                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    placeholder = { Text("Buscar productos...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Categorías
                Text(text = "Categorías", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CategoryItem(name = "Medicamentos")
                    CategoryItem(name = "Cuidado Personal")
                    CategoryItem(name = "Primeros Auxilios")
                }

                Spacer(modifier = Modifier.height(16.dp))


                Text(text = "Ofertas Especiales", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                SpecialOfferCard(offer = "Oferta 1", description = "Descuento del 20% en vitaminas")
                SpecialOfferCard(offer = "Oferta 2", description = "2x1 en productos para el cuidado de la piel")
                SpecialOfferCard(offer = "Oferta 3", description = "Envío gratis en compras mayores a $1000")

                Spacer(modifier = Modifier.height(16.dp))


                Text(text = "Productos Destacados", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ProductCard(product = "Paracetamol", price = "$120.00", rating = "4.5")
                    ProductCard(product = "Vitamina C", price = "$250.00", rating = "4.8")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ProductCard(product = "Crema Hidratante", price = "$180.00", rating = "4.2")
                    ProductCard(product = "Alcohol en Gel", price = "$80.00", rating = "4.7")
                }

                Spacer(modifier = Modifier.height(16.dp))


                FooterSection()
            }
        }
    )
}
@Composable
fun CategoryItem(name: String) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = name, color = Color.Black, fontSize = 12.sp)
        }
    }
}

@Composable
fun SpecialOfferCard(offer: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = offer, fontWeight = FontWeight.Bold)
            Text(text = description, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ProductCard(product: String, price: String, rating: String) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(text = product, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = price, color = Color(0xFF0D6EFD), fontWeight = FontWeight.Bold)
            Text(text = "⭐ $rating", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { /* Acción agregar al carrito */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Agregar al Carrito")
            }
        }
    }
}

@Composable
fun FooterSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF212529))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "FarmApp", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Tu farmacia en línea de confianza", color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Enlaces Rápidos", color = Color.White, fontWeight = FontWeight.Bold)
        Text(text = "Sobre Nosotros", color = Color.White)
        Text(text = "Contacto", color = Color.White)
        Text(text = "Política de Privacidad", color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Contáctanos", color = Color.White, fontWeight = FontWeight.Bold)
        Text(text = "Email: info@farmapp.com", color = Color.White)
        Text(text = "Teléfono: (123) 456-7890", color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "© 2024 FarmApp. Todos los derechos reservados.", color = Color.White, fontSize = 12.sp)
    }
}