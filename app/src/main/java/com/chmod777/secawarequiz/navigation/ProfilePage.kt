import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "Back",
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(16.dp))


        BasicText(
            text = "Профиль",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))


        Image(
            painter = painterResource(id = R.drawable.ic_profile),
            contentDescription = "Profile Image",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))


        BasicText(
            text = "Даниил Хохлов",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )


        BasicText(
            text = "хохлов_даниил@почта",
            color = Color.Blue
        )

        Spacer(modifier = Modifier.height(24.dp))

        BasicText(
            text = "Аккаунт",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        AccountOption(iconId = R.drawable.ic_edit, text = "Изменить профиль")
        AccountOption(iconId = R.drawable.ic_lock, text = "Изменить пароль")
        AccountOption(iconId = R.drawable.ic_notifications, text = "Уведомления")

        Spacer(modifier = Modifier.height(24.dp))

        BasicText(
            text = "Settings",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        SettingsOption(iconId = R.drawable.ic_theme, text = "Тема", value = "Тёмная")
        SettingsOption(iconId = R.drawable.ic_language, text = "Язык", value = "Русский")
        SettingsOption(iconId = R.drawable.ic_info, text = "О приложении")
    }
}

@Composable
fun AccountOption(iconId: Int, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = text,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        BasicText(text = text)
    }
}

@Composable
fun SettingsOption(iconId: Int, text: String, value: String? = null) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = text,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        BasicText(text = text)
        Spacer(modifier = Modifier.weight(1f))
        if (value != null) {
            BasicText(text = value, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
