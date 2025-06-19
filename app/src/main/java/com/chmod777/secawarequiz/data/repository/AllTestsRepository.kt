package com.chmod777.secawarequiz.data.repository

import com.chmod777.secawarequiz.R
import com.chmod777.secawarequiz.data.model.TestListItem
import com.chmod777.secawarequiz.navigation.Screen

class AllTestsRepository {
    fun getAllTestsAndGames(): List<TestListItem> {
        return listOf(
            TestListItem("quiz1", R.string.alltests_quiz1_title, R.string.alltests_quiz1_desc, Screen.Quiz.createRoute(1)),
            TestListItem("minigame1", R.string.alltests_minigame1_title, R.string.alltests_minigame1_desc, Screen.Minigame1.route),
            TestListItem("fakelogin", R.string.alltests_fakelogin_title, R.string.alltests_fakelogin_desc, Screen.FakeLoginGame.route),
            TestListItem("quiz2", R.string.alltests_quiz2_title, R.string.alltests_quiz2_desc, Screen.Quiz.createRoute(2)),
            TestListItem("quiz3", R.string.alltests_quiz3_title, R.string.alltests_quiz3_desc, Screen.Quiz.createRoute(3))

        )
    }
}
