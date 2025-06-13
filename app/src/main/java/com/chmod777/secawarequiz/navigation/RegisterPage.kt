
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RegisterPage() {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Handle back action */ }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Регистрация",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        BasicTextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEFF1F5), shape = MaterialTheme.shapes.small)
                .padding(16.dp),
            textStyle = TextStyle(fontSize = 16.sp),
            decorationBox = { innerTextField ->
                if (username.isEmpty()) {
                    Text("Имя пользователя", color = Color.Gray)
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        BasicTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEFF1F5), shape = MaterialTheme.shapes.small)
                .padding(16.dp),
            textStyle = TextStyle(fontSize = 16.sp),
            decorationBox = { innerTextField ->
                if (email.isEmpty()) {
                    Text("Email", color = Color.Gray)
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        BasicTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEFF1F5), shape = MaterialTheme.shapes.small)
                .padding(16.dp),
            textStyle = TextStyle(fontSize = 16.sp),
            decorationBox = { innerTextField ->
                if (password.isEmpty()) {
                    Text("Пароль", color = Color.Gray)
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        BasicTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEFF1F5), shape = MaterialTheme.shapes.small)
                .padding(16.dp),
            textStyle = TextStyle(fontSize = 16.sp),
            decorationBox = { innerTextField ->
                if (confirmPassword.isEmpty()) {
                    Text("Подтвердите пароль", color = Color.Gray)
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {  },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
        ) {
            Text("Зарегистрироваться", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Уже есть аккаунт? Войти",
            color = Color.Gray,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPagePreview() {
    RegisterPage()
}
