package com.chmod777.secawarequiz.navigation

object NavRoutes {

    const val LOGIN_SCREEN = "login"
    const val REGISTRATION_SCREEN = "register"
    const val PASSWORD_RESET_SCREEN = "password_reset"


    const val HOME_SCREEN = "home"
    const val ALL_TESTS_SCREEN = "all_tests"
    const val PROFILE_SCREEN = "profile"


    const val EDIT_PROFILE_SCREEN = "edit_profile"
    const val CHANGE_PASSWORD_SCREEN = "change_password"
    const val NOTIFICATIONS_SETTINGS_SCREEN = "notifications_settings"
    const val ABOUT_APP_SCREEN = "about_app"


    const val QUIZ_SCREEN_ROUTE_TEMPLATE = "test/{questionId}"
    const val QUIZ_RESULTS_SCREEN_ROUTE_TEMPLATE = "quiz_results/{score}/{totalQuestions}"
    const val QUIZ_REVIEW_SCREEN = "quiz_review_screen"


    const val MINI_GAME_SCREEN = "mini_game"
    const val MINI_GAME_RESULTS_SCREEN_ROUTE_TEMPLATE = "mini_game_results/{score}/{totalItems}"
    const val GAME_REVIEW_SCREEN = "game_review_screen"


    const val FAKE_LOGIN_GAME_SCREEN = "fake_login_game_screen"
    const val FAKE_LOGIN_RESULTS_SCREEN_ROUTE_TEMPLATE = "fake_login_results/{score}/{totalItems}"
    const val FAKE_LOGIN_REVIEW_SCREEN = "fake_login_review_screen"




}
