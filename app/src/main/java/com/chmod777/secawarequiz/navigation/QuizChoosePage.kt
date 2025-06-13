

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign

@Composable
fun QuizChoosePage() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SecAware Quiz") },
                actions = {
                    IconButton(onClick = { /* TODO: Settings action */ }) {
                        Icon(painter = painterResource(id = R.drawable.ic_settings), contentDescription = "Settings")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigation {
                BottomNavigationItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_home), contentDescription = "Home") },
                    label = { Text("Домой") },
                    selected = false,
                    onClick = { /* TODO: Home action */ }
                )
                BottomNavigationItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_profile), contentDescription = "Profile") },
                    label = { Text("Профиль") },
                    selected = false,
                    onClick = { /* TODO: Profile action */ }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Привет, Даниил!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "Выбери тест для проверки своих знаний о безопасности в интернете",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            QuizOptionsGrid()
        }
    }
}

@Composable
fun QuizOptionsGrid() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuizOptionCard(title = "Фишинг", imageRes = R.drawable.phishing)
            QuizOptionCard(title = "Пароли", imageRes = R.drawable.passwords)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuizOptionCard(title = "Общая грамотность", imageRes = R.drawable.general_literacy)
            QuizOptionCard(title = "Безопасность Данных", imageRes = R.drawable.data_security)
        }
    }
}

@Composable
fun QuizOptionCard(title: String, imageRes: Int) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .size(150.dp)
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizChoosePagePreview() {
    QuizChoosePage()
}
