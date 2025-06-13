

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LoginPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "SecAware Quiz",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Добро пожаловать!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        BasicTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF0F4F8), shape = MaterialTheme.shapes.small)
                .padding(16.dp),
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Gray),
            decorationBox = { innerTextField ->
                if (it.text.isEmpty()) {
                    Text("Имя пользователя или email", color = Color.Gray)
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        BasicTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF0F4F8), shape = MaterialTheme.shapes.small)
                .padding(16.dp),
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Gray),
            decorationBox = { innerTextField ->
                if (it.text.isEmpty()) {
                    Text("Пароль", color = Color.Gray)
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Забыли пароль?",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE0F0FF))
        ) {
            Text("Войти")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF0F4F8))
        ) {
            Text("Войти через Google")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Нет аккаунта? Зарегистрироваться",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPagePreview() {
    LoginPage()
}
