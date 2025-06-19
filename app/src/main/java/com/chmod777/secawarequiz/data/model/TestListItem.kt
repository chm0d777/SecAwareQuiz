package com.chmod777.secawarequiz.data.model

import androidx.annotation.StringRes

data class TestListItem(
    val id: String,
    @StringRes val titleResId: Int,
    @StringRes val descriptionResId: Int,
    val route: String
)
