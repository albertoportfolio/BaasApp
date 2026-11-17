package com.example.baasauth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.baasauth.ui.theme.BaasAuthTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaasAuthTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppRoot(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

/* ------------------------- BaaS: Firebase Authentication ------------------------- */

@Composable
fun AppRoot(modifier: Modifier = Modifier) {
    val auth = remember { FirebaseAuth.getInstance() }
    var userEmail by rememberSaveable { mutableStateOf(auth.currentUser?.email) }

    if (userEmail == null) {
        AuthScreen(auth, onLoggedIn = { userEmail = it }, modifier = modifier)
    } else {
        HomeScreen(
            email = userEmail!!,
            onLogout = { auth.signOut(); userEmail = null },
            modifier = modifier
        )
    }
}

@Composable
fun AuthScreen(auth: FirebaseAuth, onLoggedIn: (String?) -> Unit, modifier: Modifier = Modifier) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }

    Column(modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Autenticación (Firebase)", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(email, { email = it }, label = { Text("Email") }, singleLine = true)
        OutlinedTextField(
            password, { password = it }, label = { Text("Contraseña (mín. 6)") },
            singleLine = true, visualTransformation = PasswordVisualTransformation()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                auth.signInWithEmailAndPassword(email.trim(), password)
                    .addOnSuccessListener { onLoggedIn(it.user?.email) }
                    .addOnFailureListener { message = "Login fallido: ${it.message}" }
            }) { Text("Iniciar sesión") }
            OutlinedButton(onClick = {
                auth.createUserWithEmailAndPassword(email.trim(), password)
                    .addOnSuccessListener { onLoggedIn(it.user?.email) }
                    .addOnFailureListener { message = "Registro fallido: ${it.message}" }
            }) { Text("Registrarse") }
        }
        if (message.isNotEmpty()) Text(message, color = MaterialTheme.colorScheme.error)
    }
}

/* ------------------------- State + State Hoisting ------------------------- */

@Composable
fun HomeScreen(email: String, onLogout: () -> Unit, modifier: Modifier = Modifier) {

    var savedCounter by rememberSaveable { mutableIntStateOf(0) }

    var normalCounter by remember { mutableIntStateOf(0) }

    Column(
        modifier.padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Bienvenido: $email")
        Button(onClick = onLogout) { Text("Cerrar sesión") }

        HorizontalDivider()
        CounterSection(
            savedCount = savedCounter,
            normalCount = normalCounter,
            onIncrement = { savedCounter++; normalCounter++ }
        )

        HorizontalDivider()
        ExceptionsDemo()
    }
}


@Composable
fun CounterSection(savedCount: Int, normalCount: Int, onIncrement: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("Contador rememberSaveable (sobrevive): $savedCount")
        Text("Contador remember (no sobrevive): $normalCount")
        Button(onClick = onIncrement) { Text("Incrementar ambos") }
    }
}



@Composable
fun ExceptionsDemo() {
    var lightOn by rememberSaveable { mutableStateOf(false) }
    var numberText by rememberSaveable { mutableStateOf("") }
    var word by rememberSaveable { mutableStateOf("") }
    var result by rememberSaveable { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Excepciones", style = MaterialTheme.typography.titleMedium)

        Row {
            Switch(lightOn, { lightOn = it })
            Spacer(Modifier.width(8.dp))
            Button(onClick = { result = validateLight(lightOn) }) { Text("Comprobar luz") }
        }

        OutlinedTextField(numberText, { numberText = it }, label = { Text("Número") }, singleLine = true)
        Button(onClick = { result = validateNumber(numberText) }) { Text("Comprobar número") }

        OutlinedTextField(word, { word = it }, label = { Text("Palabra (máx. 10)") }, singleLine = true)
        Button(onClick = { result = validateWord(word) }) { Text("Comprobar palabra") }

        Text(result)
    }
}