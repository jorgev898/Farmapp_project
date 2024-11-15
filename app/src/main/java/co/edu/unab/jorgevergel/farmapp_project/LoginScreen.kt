package co.edu.unab.jorgevergel.farmapp_project

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showResetPasswordDialog by remember { mutableStateOf(false) }
    var resetEmail by remember { mutableStateOf("") }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0E0E0))
            .padding(0.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Iniciar sesión",
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(onClick = { showResetPasswordDialog = true }) {
                            Text("¿Olvidaste tu contraseña?", color = Color.Blue)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                                        // Redirigir a MainActivity
                                        val intent = Intent(context, MainActivity::class.java)
                                        context.startActivity(intent)
                                    } else {
                                        Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                        Log.e("LoginScreen", "Error de autenticación", task.exception)
                                    }
                                }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0D6EFD),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Iniciar sesión")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("¿No tienes una cuenta?")
                        TextButton(onClick = { navController.navigate("register") }) {
                            Text("Regístrate", color = Color.Blue)
                        }
                    }
                }
            }
        }
    }

    // Dialogo para restablecer la contraseña
    if (showResetPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showResetPasswordDialog = false },
            title = { Text("Restablecer Contraseña") },
            text = {
                Column {
                    Text("Introduce tu correo electrónico y te enviaremos un enlace para restablecer la contraseña.")
                    OutlinedTextField(
                        value = resetEmail,
                        onValueChange = { resetEmail = it },
                        label = { Text("Correo electrónico") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (resetEmail.isNotEmpty()) {
                            auth.sendPasswordResetEmail(resetEmail)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(context, "Enlace de restablecimiento enviado", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                    showResetPasswordDialog = false
                                }
                        } else {
                            Toast.makeText(context, "Por favor ingresa un correo electrónico", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Enviar enlace")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetPasswordDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
