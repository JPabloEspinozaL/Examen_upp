package com.example.examen_upp

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke // <-- Import para el borde
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility // <-- Import para ícono
import androidx.compose.material.icons.filled.VisibilityOff // <-- Import para ícono
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController // <-- Import para Navegación
import androidx.navigation.compose.NavHost // <-- Import para Navegación
import androidx.navigation.compose.composable // <-- Import para Navegación
import androidx.navigation.compose.rememberNavController // <-- Import para Navegación

// --- 1. Definición de la Paleta de Colores ---

// Color principal de la marca, basado en tu imagen (azul)
val ColorPrincipal = Color(0xFF1C6BFF)
// Color para mensajes de error, según lo solicitado
val ColorErrorRojo = Color(0xFFD32F2F)
// Colores adicionales para la UI
val ColorTexto = Color(0xFF1D1D1D)
val ColorTextoSecundario = Color(0xFF6E6E6E)
val ColorFondoBotonSocial = Color(0xFFF1F1F1)
val ColorBordeCampo = Color(0xFFBDBDBD)


// --- 2. Tema de la Aplicación ---
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = ColorPrincipal,
            error = ColorErrorRojo,
            onSurface = ColorTexto,
            onSurfaceVariant = ColorTextoSecundario
        ),
        shapes = Shapes(
            medium = RoundedCornerShape(10.dp) // Bordes redondeados para botones y campos
        ),
        typography = Typography(
            // Títulos
            headlineMedium = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = ColorTexto
            ),
            // Subtítulos
            titleLarge = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                color = ColorTextoSecundario
            ),
            // Texto de botones
            labelLarge = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            )
        ),
        content = content
    )
}

// --- 3. Actividad Principal ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

// --- 4. Navegación de la App ---
object Routes {
    const val LOGIN = "login"
    const val SIGNUP = "signup"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(navController = navController)
        }
        composable(Routes.SIGNUP) {
            SignupScreen(navController = navController)
        }
    }
}

// --- 5. Pantalla de Login ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {

    // --- Estado del formulario ---
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    // --- Estado de la validación ---
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }

    // --- Lógica de Validación (se define aquí) ---
    fun validateLogin(): Boolean {
        emailError = null
        passwordError = null
        if (email.isBlank()) {
            emailError = "El correo no puede estar vacío"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Formato de correo inválido"
        }
        if (password.isBlank()) {
            passwordError = "La contraseña no puede estar vacía"
        }
        return emailError == null && passwordError == null
    }

    // --- UI ---
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()), // Permite scroll si el contenido no cabe
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // --- Títulos ---
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Welcome back!",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- Campo de Email ---
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null // Limpia el error al escribir
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                isError = emailError != null,
                supportingText = {
                    if (emailError != null) {
                        Text(emailError!!, color = MaterialTheme.colorScheme.error)
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Campo de Contraseña ---
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null // Limpia el error al escribir
                },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = passwordError != null,
                supportingText = {
                    if (passwordError != null) {
                        Text(passwordError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                }
            )

            // --- Link "Forgot Password?" ---
            TextButton(
                onClick = { /* TODO: Lógica de olvidar contraseña */ },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Forgot Password?", color = ColorPrincipal)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Botón de Login ---
            Button(
                onClick = {
                    // ¡¡La validación se llama AQUÍ!!
                    if (validateLogin()) {
                        // Lógica de login exitosa
                        // (p.ej. llamar a un ViewModel, API, etc.)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Login", style = MaterialTheme.typography.labelLarge)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Link de Signup ---
            ClickableText(
                text = AnnotatedString("Don't have an account? ") + AnnotatedString(
                    text = "Signup",
                    spanStyle = SpanStyle(
                        color = ColorPrincipal,
                        fontWeight = FontWeight.Bold
                    )
                ),
                onClick = { offset ->
                    // Comprueba si se hizo clic en la palabra "Signup"
                    if (offset > 24) {
                        navController.navigate(Routes.SIGNUP)
                    }
                },
                style = TextStyle(
                    fontSize = 16.sp,
                    color = ColorTextoSecundario
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Divisor "Or" ---
            OrDivider()

            Spacer(modifier = Modifier.height(24.dp))

            // --- Botones Sociales ---
            SocialLoginButton(
                text = "Login with Facebook",
                iconResId = "f", // Placeholder para el ícono
                onClick = { /* TODO: Lógica de Facebook Login */ }
            )
            Spacer(modifier = Modifier.height(16.dp))
            SocialLoginButton(
                text = "Login with Google",
                iconResId = "G", // Placeholder para el ícono
                onClick = { /* TODO: Lógica de Google Login */ }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// --- 6. Pantalla de Signup ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {

    // --- Estado del formulario ---
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    // --- Estado de la validación ---
    var nameError by rememberSaveable { mutableStateOf<String?>(null) }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }
    var confirmPasswordError by rememberSaveable { mutableStateOf<String?>(null) }

    // --- Lógica de Validación (se define aquí) ---
    fun validateSignup(): Boolean {
        nameError = if (name.isBlank()) "El nombre no puede estar vacío" else null

        emailError = if (email.isBlank()) {
            "El correo no puede estar vacío"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Formato de correo inválido"
        } else {
            null
        }

        passwordError = if (password.isBlank()) {
            "La contraseña no puede estar vacía"
        } else if (password.length < 6) {
            "La contraseña debe tener al menos 6 caracteres"
        } else {
            null
        }

        confirmPasswordError = if (confirmPassword.isBlank()) {
            "Confirma tu contraseña"
        } else if (password != confirmPassword) {
            "Las contraseñas no coinciden"
        } else {
            null
        }

        return nameError == null && emailError == null && passwordError == null && confirmPasswordError == null
    }

    // --- UI ---
    Scaffold(
        topBar = {
            TopAppBar(
                title = {}, // Sin título en la barra
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = ColorTexto
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent // Fondo transparente
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- Títulos ---
            Text(
                text = "Create your account",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Signup",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- Campo de Nombre ---
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = null
                },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = nameError != null,
                supportingText = {
                    if (nameError != null) {
                        Text(nameError!!, color = MaterialTheme.colorScheme.error)
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // --- Campo de Email ---
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                isError = emailError != null,
                supportingText = {
                    if (emailError != null) {
                        Text(emailError!!, color = MaterialTheme.colorScheme.error)
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // --- Campo de Contraseña ---
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = passwordError != null,
                supportingText = {
                    if (passwordError != null) {
                        Text(passwordError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, if (passwordVisible) "Ocultar" else "Mostrar")
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // --- Campo de Confirmar Contraseña ---
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = null
                },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = confirmPasswordError != null,
                supportingText = {
                    if (confirmPasswordError != null) {
                        Text(confirmPasswordError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                trailingIcon = {
                    val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = image, if (confirmPasswordVisible) "Ocultar" else "Mostrar")
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Botón de Signup ---
            Button(
                onClick = {
                    // ¡¡La validación se llama AQUÍ!!
                    if (validateSignup()) {
                        // Lógica de registro exitosa
                        // ...
                        // Navegar de vuelta al login
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Signup", style = MaterialTheme.typography.labelLarge)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Divisor "Or" ---
            OrDivider()

            Spacer(modifier = Modifier.height(24.dp))

            // --- Botones Sociales ---
            SocialLoginButton(
                text = "Login with Facebook",
                iconResId = "f", // Placeholder
                onClick = { /* TODO: Lógica de Facebook Login */ }
            )
            Spacer(modifier = Modifier.height(16.dp))
            SocialLoginButton(
                text = "Login with Google",
                iconResId = "G", // Placeholder
                onClick = { /* TODO: Lógica de Google Login */ }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


// --- 7. Componentes Reutilizables ---

/**
 * Un divisor con el texto "Or" en el medio, como en el diseño.
 */
@Composable
fun OrDivider() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier.weight(1f),
            color = ColorBordeCampo,
            thickness = 1.dp
        )
        Text(
            text = "Or",
            modifier = Modifier.padding(horizontal = 16.dp),
            color = ColorTextoSecundario
        )
        Divider(
            modifier = Modifier.weight(1f),
            color = ColorBordeCampo,
            thickness = 1.dp
        )
    }
}

/**
 * Botón para Login Social (Google, Facebook)
 * @param text El texto del botón (ej. "Login with Google")
 * @param iconResId Un placeholder para el ícono. En un proyecto real, usarías un Painter.
 * @param onClick La acción a ejecutar.
 */
@Composable
fun SocialLoginButton(
    text: String,
    iconResId: String, // Placeholder. Idealmente sería un @DrawableRes
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = ColorFondoBotonSocial
        ),
        border = BorderStroke(1.dp, ColorBordeCampo) // <-- Aquí se usa BorderStroke
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Placeholder para el ícono
            Text(
                text = iconResId,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = ColorPrincipal,
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(
                text = text,
                color = ColorTexto,
                fontSize = 16.sp
            )
        }
    }
}