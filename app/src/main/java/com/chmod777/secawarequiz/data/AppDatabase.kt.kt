package com.chmod777.secawarequiz.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Question::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "secaware_quiz_database"
                )
                    .addCallback(AppDatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    val questionDao = database.questionDao()
                    questionDao.insertQuestion(
                        Question(
                            url = "http://sbebrbank.com",
                            isSecure = false,
                            explanation = "HTTP небезопасен, нет SSL-сертификата. Злоумышленник может перехватить ваши данные. Домен 'sbebrbank.com' может быть фишинговым, даже если кажется похожим на настоящий сайт."
                        )
                    )
                    questionDao.insertQuestion(
                        Question(
                            url = "https://online.sberbank.ru",
                            isSecure = true,
                            explanation = "HTTPS использует SSL/TLS для шифрования соединения, это безопасно. Всегда проверяйте наличие 'https://'."
                        )
                    )
                    questionDao.insertQuestion(
                        Question(
                            url = "http://support.tbank.net/reset-password",
                            isSecure = false,
                            explanation = "URL содержит 'http' вместо 'https', что делает его небезопасным. Также, доменное имя 'tbank.net' может быть фишинговым, даже если кажется похожим на настоящий домен."
                        )
                    )
                    questionDao.insertQuestion(
                        Question(
                            url = "https://myaccount.paypal.com.login.xyz/verify",
                            isSecure = false,
                            explanation = "Несмотря на 'https', настоящий домен находится в конце: 'login.xyz'. 'paypal.com' здесь является поддоменом, что указывает на фишинг. Всегда смотрите на корневой домен."
                        )
                    )
                }
            }
        }
    }
}
