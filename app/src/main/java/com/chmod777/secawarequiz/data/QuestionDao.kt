package com.chmod777.secawarequiz.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: UrlCheckQuestion)

    @Query("SELECT * FROM url_check_questions")
    fun getAllQuestions(): Flow<List<UrlCheckQuestion>>

    @Query("SELECT * FROM url_check_questions WHERE id = :id")
    suspend fun getQuestionById(id: Int): UrlCheckQuestion?

    @Query("DELETE FROM url_check_questions")
    suspend fun deleteAllQuestions()

    @Query("SELECT * FROM quiz_questions")
    fun getAllQuizQuestions(): Flow<List<QuizQuestion>>

    @Query("SELECT * FROM quiz_questions WHERE id = :id")
    fun getQuizQuestionById(id: Int): Flow<QuizQuestion?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizQuestion(question: QuizQuestion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllQuizQuestions(questions: List<QuizQuestion>)

    @Query("SELECT * FROM quiz_questions WHERE quizGroupId = :groupId ORDER BY id ASC")
    fun getQuestionsByGroupId(groupId: String): Flow<List<QuizQuestion>>
}
