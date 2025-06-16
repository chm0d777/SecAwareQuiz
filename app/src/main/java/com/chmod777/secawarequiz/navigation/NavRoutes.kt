package com.chmod777.secawarequiz.navigation

object NavRoutes {
    const val LOGIN_SCREEN = "login"
    const val REGISTRATION_SCREEN = "register"
    const val HOME_SCREEN = "home"
    const val PROFILE_SCREEN = "profile"
    const val TEST_SCREEN = "test/{questionId}"
    fun testScreen(questionId: Int) = "test/$questionId"
    const val QUIZ_RESULTS_SCREEN = "quiz_results/{score}/{totalQuestions}"
    fun quizResultsScreen(score: Int, totalQuestions: Int) = "quiz_results/$score/$totalQuestions"
    const val MINI_GAME_SCREEN = "mini_game"
    const val MINI_GAME_RESULTS_SCREEN = "mini_game_results/{score}/{totalItems}"
    fun miniGameResultsScreen(score: Int, totalItems: Int) = "mini_game_results/$score/$totalItems"
    const val FAKE_LOGIN_GAME_SCREEN = "fake_login_game_screen"
    const val PASSWORD_RESET_SCREEN = "password_reset"
    const val ALL_TESTS_SCREEN = "all_tests"
}
