package co.edu.unab.jorgevergel.farmapp_project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CartScreen(cartItems: List<CartItem>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Carrito de Compras",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0D6EFD),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (cartItems.isEmpty()) {
            Text("El carrito está vacío.")
        } else {
            LazyColumn {
                items(cartItems) { cartItem ->
                    CartItemCard(cartItem)
                }
            }

            // Calcula el total del carrito
            val totalPrice = cartItems.sumOf { it.price * it.quantity }

            Spacer(modifier = Modifier.height(16.dp))

            // Muestra el total en la interfaz
            Text(
                text = "Total: ${formatPrice(totalPrice)}",  // Formatea el total como moneda
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF0D6EFD),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End // Alinea el total a la derecha
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Acción para procesar compra */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD))
            ) {
                Text("Procesar Compra", color = Color.White)
            }
        }
    }
}

@Composable
fun CartItemCard(cartItem: CartItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = "Producto",
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cartItem.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Cantidad: ${cartItem.quantity}",
                    fontSize = 14.sp
                )
                Text(
                    text = "Precio: ${formatPrice(cartItem.quantity * cartItem.price)}", // Formatea el precio del producto
                    fontSize = 14.sp,
                    color = Color(0xFF0D6EFD)
                )
            }
        }
    }
}
fun formatPrice(price: Double): String {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO")) // Para formato colombiano
    return numberFormat.format(price)
}