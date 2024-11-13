package co.edu.unab.jorgevergel.farmapp_project


import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    // Método para obtener los productos de Firebase
    fun getProducts() = liveData {
        try {
            val productsSnapshot = firestore.collection("products").get().await()
            val productsList = productsSnapshot.documents.map { document ->
                Product(
                    id = document.id,
                    name = document.getString("name") ?: "",
                    price = document.getString("price") ?: "",
                    imageUrl = document.getString("imageUrl") ?: "",
                    category = document.getString("category") ?: "" // Obtiene la categoría
                )
            }
            emit(productsList)
        } catch (e: Exception) {
            emit(emptyList<Product>())
        }
    }
}

data class Product(
    val id: String,
    val name: String,
    val price: String,
    val imageUrl: String, // URL de la imagen
    val category: String  // Nueva propiedad para la categoría
)