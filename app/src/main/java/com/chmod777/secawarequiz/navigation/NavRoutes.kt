package com.chmod777.secawarequiz.navigation

object NavRoutes {
    const val LOGIN_SCREEN = "login"
    const val REGISTRATION_SCREEN = "register"
    const val HOME_SCREEN = "home"
    const val TEST_SCREEN = "test/{questionId}"
    fun testScreen(questionId: Int) = "test/$questionId"
    const val MINI_GAME_SCREEN = "mini_game"
}
