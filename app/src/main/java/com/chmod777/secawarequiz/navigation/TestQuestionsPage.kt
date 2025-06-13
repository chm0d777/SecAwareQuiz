
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TestQuestionsPage() {
    var selectedOption by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { /* Handle back action */ }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Тест", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Вопрос 1/5", fontSize = 14.sp, color = Color.Gray)
        LinearProgressIndicator(
            progress = 0.2f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            color = Color.Blue,
            trackColor = Color.LightGray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Что из перечисленного не является распространенным признаком фишингового письма?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.selectableGroup()) {
            val options = listOf(
                "Обычное приветствие типа “Уважаемый клиент”",
                "Настойчивые или угрожающие высказывания",
                "Запрос личной информации",
                "Грамматически правильный и профессиональный тон"
            )

            options.forEachIndexed { index, text ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .selectable(
                            selected = (selectedOption == index),
                            onClick = { selectedOption = index }
                        )
                        .background(
                            if (selectedOption == index) Color(0xFFE0F7FA) else Color.Transparent
                        )
                        .padding(16.dp)
                ) {
                    RadioButton(
                        selected = (selectedOption == index),
                        onClick = { selectedOption = index }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Следующий вопрос")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTestQuestionsPage() {
    TestQuestionsPage()
}
