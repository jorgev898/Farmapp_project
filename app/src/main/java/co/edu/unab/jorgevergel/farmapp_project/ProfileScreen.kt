package co.edu.unab.jorgevergel.farmapp_project

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import coil3.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth


data class UserProfile(val name: String = "", val email: String = "", val phone: String = "", val profileImageUrl: String = "")

@Composable
fun ProfileScreen() {
    // Estado para controlar si los campos son editables o no
    var isEditable by remember { mutableStateOf(false) }

    // Estado para los valores de los campos
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var profileImageUrl by remember { mutableStateOf("") }

    // Recuperar la información de Firestore
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    val userDocRef = db.collection("users").document(userId)

    // Cargar los datos del usuario (en un Composable que ejecute la carga solo una vez)
    LaunchedEffect(Unit) {
        userDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot != null && documentSnapshot.exists()) {
                val userProfile = documentSnapshot.toObject<UserProfile>()
                if (userProfile != null) {
                    name = userProfile.name
                    email = userProfile.email
                    phone = userProfile.phone
                    profileImageUrl = userProfile.profileImageUrl
                }
            } else {
                Log.d("ProfileScreen", "No such document!")
            }
        }
    }

    // Función para actualizar el perfil del usuario en Firestore
    fun updateUserProfile(db: FirebaseFirestore, userId: String, name: String, email: String, phone: String, profileImageUrl: String) {
        val userDocRef = db.collection("users").document(userId)
        val updatedProfile = hashMapOf(
            "name" to name,
            "email" to email,
            "phone" to phone,
            "profileImageUrl" to profileImageUrl
        )

        userDocRef.set(updatedProfile)
            .addOnSuccessListener {
                Log.d("ProfileScreen", "Profile updated successfully!")
            }
            .addOnFailureListener { e ->
                Log.w("ProfileScreen", "Error updating profile", e)
            }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            // Encabezado y avatar
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0D6EFD))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Mi Perfil", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Mostrar la imagen de perfil usando Coil
                    if (profileImageUrl.isNotEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(profileImageUrl),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(72.dp)
                                .background(Color.White, shape = CircleShape)
                                .padding(4.dp),
                            alignment = Alignment.Center,
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Placeholder si no hay URL
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(72.dp)
                                .background(Color.White, shape = CircleShape)
                                .padding(4.dp),
                            tint = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Nombre
            OutlinedTextField(
                value = name,
                onValueChange = { if (isEditable) name = it },
                label = { Text("Nombre") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "Nombre")
                },
                modifier = Modifier.fillMaxWidth(),
                readOnly = !isEditable
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de Email
            OutlinedTextField(
                value = email,
                onValueChange = { if (isEditable) email = it },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "Email")
                },
                modifier = Modifier.fillMaxWidth(),
                readOnly = !isEditable
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de Teléfono
            OutlinedTextField(
                value = phone,
                onValueChange = { if (isEditable) phone = it },
                label = { Text("Teléfono") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = "Teléfono")
                },
                modifier = Modifier.fillMaxWidth(),
                readOnly = !isEditable
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de actualizar perfil
            Button(
                onClick = {
                    if (isEditable) {
                        // Guardar los datos actualizados en Firestore
                        updateUserProfile(db, userId, name, email, phone, profileImageUrl)
                    }
                    // Cambiar el estado de edición
                    isEditable = !isEditable
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditable) "Guardar Perfil" else "Actualizar Perfil", color = Color.White)
            }
            
        }
    }
}

