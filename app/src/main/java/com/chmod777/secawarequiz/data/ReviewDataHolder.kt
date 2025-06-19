package com.chmod777.secawarequiz.data

import com.chmod777.secawarequiz.data.model.AnsweredFakeLoginItemDetails
import com.chmod777.secawarequiz.data.model.AnsweredGameItemDetails
import com.chmod777.secawarequiz.data.model.AnsweredQuestionDetails

object ReviewDataHolder {
    var answeredQuestions: List<AnsweredQuestionDetails>? = null
    var answeredGameItems: List<AnsweredGameItemDetails>? = null
    var answeredFakeLoginItems: List<AnsweredFakeLoginItemDetails>? = null

    fun clearQuizReviewData() {
        answeredQuestions = null
    }

    fun clearGameReviewData() {
        answeredGameItems = null
    }

    fun clearFakeLoginReviewData() {
        answeredFakeLoginItems = null
    }


    fun clearAllReviewData() {
        answeredQuestions = null
        answeredGameItems = null
        answeredFakeLoginItems = null
    }
}
