package com.chmod777.secawarequiz.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(onTestSelected: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Тесты по повышению осведомлённости о фишинге",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = { onTestSelected(1) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Тест: Фишинг (URL)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { },
            enabled = false,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Тест: Пароли (скоро)")
        }
    }
}