package com.chmod777.secawarequiz.ui.screens.minigames

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.chmod777.secawarequiz.R
import com.chmod777.secawarequiz.data.model.AnsweredFakeLoginItemDetails
import com.chmod777.secawarequiz.viewmodels.FakeLoginReviewViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FakeLoginReviewScreen(
    navController: NavHostController,
    reviewViewModel: FakeLoginReviewViewModel = viewModel()
) {
    val answeredItems by reviewViewModel.answeredItems.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.fake_login_review_title)) },
                navigationIcon = {
                    IconButton(onClick = {
                        reviewViewModel.clearReviewData()
                        navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.common_back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        if (answeredItems.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).background(MaterialTheme.colorScheme.background).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    stringResource(R.string.review_answers_no_data),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                itemsIndexed(answeredItems) { index, item ->
                    FakeLoginReviewItem(item = item, itemNumber = index + 1)
                    if (index < answeredItems.size - 1) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun FakeLoginReviewItem(item: AnsweredFakeLoginItemDetails, itemNumber: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(

                text = stringResource(R.string.review_answers_question_number, itemNumber),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "${item.fakeLoginItem.serviceName}: ${item.fakeLoginItem.description}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))

            val userAnswerString = if (item.userAnswerWasFake) stringResource(R.string.fl_review_value_fake) else stringResource(R.string.fl_review_value_real)
            Text(
                stringResource(R.string.fl_review_your_choice, userAnswerString),
                style = MaterialTheme.typography.bodyMedium,
                color = if (item.wasCorrect) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (!item.wasCorrect) {
                val actualAnswerString = if (item.fakeLoginItem.isFake) stringResource(R.string.fl_review_value_fake) else stringResource(R.string.fl_review_value_real)
                Text(
                    stringResource(R.string.fl_review_actual_answer, actualAnswerString),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                stringResource(R.string.review_answers_explanation_label),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = item.fakeLoginItem.explanation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 8.dp)
            )

            if (item.fakeLoginItem.isFake && item.fakeLoginItem.elementsToSpot.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    stringResource(R.string.fl_review_elements_label),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                item.fakeLoginItem.elementsToSpot.forEach { element ->
                    Text(
                        text = "- $element",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}
