package com.chmod777.secawarequiz.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [UrlCheckQuestion::class, GameItem::class, FakeLoginGameItem::class],
    version = 1, // Consider incrementing version if schema changes significantly for existing users
    exportSchema = false
)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao
    abstract fun gameItemDao(): GameItemDao
    abstract fun fakeLoginGameItemDao(): FakeLoginGameItemDao

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
                    .fallbackToDestructiveMigration()
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
                    questionDao.deleteAllQuestions()
                    val urlCheckQuestions = listOf(
                        UrlCheckQuestion(
                            id = 0,
                            url = "http://sbebrbank.com/login",
                            isSecure = false,
                            explanation = "HTTP небезопасен, нет SSL-сертификата. Домен 'sbebrbank.com' похож на фишинговый (опечатка в 'sberbank')."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "https://online.sberbank.ru",
                            isSecure = true,
                            explanation = "HTTPS использует SSL/TLS для шифрования. Домен 'sberbank.ru' является официальным. Это безопасно."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "http://support.tbank.ru/reset-password",
                            isSecure = false,
                            explanation = "URL использует HTTP. Домен 'tbank.ru' может быть легитимным, но для сброса пароля всегда должен быть HTTPS."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "https://myaccount.paypal.com.login.xyz/verify",
                            isSecure = false,
                            explanation = "Несмотря на 'https', настоящий домен здесь 'login.xyz'. 'paypal.com' является поддоменом, что типично для фишинга."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "Email от 'sberbank@mail-support.ru': 'Войдите для проверки транзакции'. Ссылка ведет на sberbank.confirm-transaction.com",
                            isSecure = false,
                            explanation = "Адрес отправителя 'mail-support.ru' не является официальным доменом Сбербанка. Ссылка также подозрительна."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "SMS: 'Ваш аккаунт VK был взломан! Срочно смените пароль: bit.ly/vk-reset'",
                            isSecure = false,
                            explanation = "Сообщения о взломе с требованием срочно перейти по короткой ссылке (bit.ly) часто являются фишингом."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "Email: 'Prived! Ti vigral приз! Nazmi suda: super-priz.info'",
                            isSecure = false,
                            explanation = "Плохая грамматика ('Prived', 'vigral', 'suda') и сомнительный домен 'super-priz.info' указывают на фишинг."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "Уведомление от 'Gosuslugi': 'Вам начислен штраф. Оплатите по ссылке: g0süs1ugi.ru/shtraf'",
                            isSecure = false,
                            explanation = "Домен 'g0süs1ugi.ru' использует опечатки (0 вместо o, ü, 1 вместо l) для имитации официального сайта Госуслуг (gosuslugi.ru)."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "Сообщение от 'Ozon': 'Ваш заказ #12345 отменен. Для возврата средств введите данные карты: ozon-return.payment-secure.net'",
                            isSecure = false,
                            explanation = "Поддельный сайт 'ozon-return.payment-secure.net' пытается получить данные вашей карты. Ozon не будет запрашивать полные данные карты для возврата."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "Пароль '12345678'. Это надежный пароль?",
                            isSecure = false,
                            explanation = "Простые последовательности цифр легко угадать. Надежный пароль должен быть длинным, сложным и уникальным."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "Использовать один и тот же пароль для email, банка и соцсетей. Это безопасно?",
                            isSecure = false,
                            explanation = "Если один сервис взломают, все ваши аккаунты с этим паролем окажутся под угрозой. Используйте уникальные пароли."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "Пароль 'МояСобакаШарик1995!'. Достаточно ли он надежен?",
                            isSecure = false,
                            explanation = "Хотя он длиннее и содержит разные символы, использование личной информации (кличка питомца, год) делает его уязвимым. Лучше избегать легко угадываемых личных данных."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "Включение двухфакторной аутентификации (2FA) для важных аккаунтов. Это хорошая практика?",
                            isSecure = true,
                            explanation = "2FA значительно повышает безопасность, требуя второй фактор (например, код из SMS или приложения) помимо пароля."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "Сайт 'http://onlineshop.biz' запрашивает данные вашей кредитной карты. Безопасно ли вводить?",
                            isSecure = false,
                            explanation = "Отсутствие HTTPS (замочка в браузере) означает, что данные передаются в незашифрованном виде и могут быть перехвачены."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "Браузер показывает предупреждение 'Незащищенное соединение' для сайта, но он выглядит нормально. Можно ли доверять?",
                            isSecure = false,
                            explanation = "Предупреждения браузера о безопасности следует воспринимать серьезно. Они указывают на проблемы с SSL-сертификатом или другие угрозы."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "Сайт 'https://bank-online.secure-payment.com'. На что обратить внимание в первую очередь?",
                            isSecure = false,
                            explanation = "Несмотря на HTTPS, основной домен 'secure-payment.com' может не принадлежать вашему банку. Всегда проверяйте, что основной домен является официальным доменом банка."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "Видите значок замка в адресной строке браузера. Означает ли это, что сайт на 100% безопасен от фишинга?",
                            isSecure = false,
                            explanation = "Замок означает, что соединение с сайтом зашифровано. Однако фишинговые сайты тоже могут использовать HTTPS и иметь замок. Важно также проверять сам домен."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "Ввод логина и пароля на сайте 'https://vk.com-login-page.ru/auth'",
                            isSecure = false,
                            explanation = "Это пример typosquatting и обманчивого поддомена. Настоящий домен здесь 'vk.com-login-page.ru', а не 'vk.com'."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "SMS от 'PochtaRossii': Ваша посылка №12345678 задержана. Для уточнения пройдите по ссылке: pochta-track.info/status",
                            isSecure = false,
                            explanation = "Официальный сайт Почты России 'pochta.ru'. Домен 'pochta-track.info' является поддельным и используется для фишинга."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "Переход на сайт 'gosuslugi.ru.com' для входа в личный кабинет.",
                            isSecure = false,
                            explanation = "Официальный сайт Госуслуг 'gosuslugi.ru'. Домен 'gosuslugi.ru.com' является поддельным, имитируя официальный адрес."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "Покупатель на Avito прислал ссылку на 'avito-oplata.cc/12345' для получения денег за товар, просит ввести данные карты, включая CVC.",
                            isSecure = false,
                            explanation = "Avito не запрашивает CVC-код для получения оплаты. Это мошенническая схема для кражи данных карты. Все операции должны проходить внутри официального чата/интерфейса Avito."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "Сообщение от друга ВКонтакте: 'Привет, срочно нужны деньги, скинь 2000р на эту карту [номер карты], вечером верну! Не могу говорить сейчас.'",
                            isSecure = false,
                            explanation = "Аккаунты друзей могут быть взломаны. Всегда проверяйте такие просьбы через другой канал связи (звонок, SMS), прежде чем переводить деньги."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "Реклама iPhone 15 за полцены на сайте 'mvideo-bestprice.online'. Сайт выглядит как официальный.",
                            isSecure = false,
                            explanation = "Слишком низкая цена и неофициальный домен ('mvideo-bestprice.online' вместо 'mvideo.ru') указывают на мошеннический сайт."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "Вход в интернет-банк через общедоступный Wi-Fi 'CoffeeShop_Free' в популярной кофейне.",
                            isSecure = false,
                            explanation = "Общедоступные Wi-Fi сети могут быть небезопасны. Избегайте выполнения финансовых операций или ввода чувствительных данных через них. Используйте мобильный интернет или VPN."
                        ),
                        UrlCheckQuestion(
                            id = 0,
                            url = "SMS с номера +79xxxxxxxxx: 'Ваша карта Тинькофф заблокирована из-за подозрительной активности. Позвоните на [номер телефона]. С уважением, Служба Безопасности.'",
                            isSecure = false,
                            explanation = "Банки обычно отправляют SMS с официальных коротких номеров или имен отправителей (например, Tinkoff). SMS с обычного мобильного номера с просьбой перезвонить на неизвестный номер является признаком мошенничества."
                        )
                    )
                    urlCheckQuestions.forEach { questionDao.insertQuestion(it) }

                    val gameItemDao = database.gameItemDao()
                    gameItemDao.deleteAllGameItems()
                    val gameItems = listOf(
                        GameItem(
                            id = 0,
                            scenario = "Вам пришло SMS: 'Ваша карта заблокирована. Для разблокировки перейдите по ссылке sberbank-unlock-card.xyz и следуйте инструкциям.' Ваши действия?",
                            options = listOf(
                                "Перейти по ссылке и ввести данные карты.",
                                "Удалить SMS и ничего не делать.",
                                "Позвонить по официальному номеру банка и уточнить информацию."
                            ),
                            correctOptionIndex = 2,
                            explanation = "Никогда не переходите по ссылкам из подозрительных SMS. Мошенники часто используют поддельные сайты для кражи данных. Лучше всего позвонить в банк по официальному номеру."
                        ),
                        GameItem(
                            id = 0,
                            scenario = "На почту пришло письмо от 'GosUslugi' с темой 'Штраф за нарушение ПДД' и вложенным файлом 'квитанция.docx'. В письме просят срочно оплатить штраф, иначе он удвоится. Что предпримете?",
                            options = listOf(
                                "Открыть файл и оплатить, чтобы избежать проблем.",
                                "Проверить наличие штрафа на официальном сайте Госуслуг или ГИБДД.",
                                "Переслать письмо другу-юристу, чтобы он разобрался."
                            ),
                            correctOptionIndex = 1,
                            explanation = "Фишинговые письма часто имитируют официальные органы. Не открывайте вложения из подозрительных писем, они могут содержать вирусы. Проверяйте информацию на официальных ресурсах."
                        ),
                        GameItem(
                            id = 0,
                            scenario = "В социальной сети 'VKontakte' вам пишет старый друг и просит срочно одолжить 1000 рублей на карту, обещает вернуть завтра. Его страница выглядит как обычно. Как поступить?",
                            options = listOf(
                                "Конечно, перевести деньги другу.",
                                "Написать другу в другой мессенджер или позвонить, чтобы убедиться, что это действительно он.",
                                "Проигнорировать сообщение, вдруг это спам."
                            ),
                            correctOptionIndex = 1,
                            explanation = "Аккаунты в соцсетях часто взламывают для рассылки просьб о деньгах. Прежде чем переводить средства, свяжитесь с другом альтернативным способом для подтверждения."
                        ),
                        GameItem(
                            id = 0,
                            scenario = "Вы ищете новую работу и получаете email от 'Крупная Международная Компания ™' с предложением высокооплачиваемой удаленной работы. Для оформления просят перевести 500 рублей за 'проверку документов'.",
                            options = listOf(
                                "Отличная возможность, нужно перевести деньги.",
                                "Звучит подозрительно, настоящие работодатели не просят денег за трудоустройство.",
                                "Попросить их вычесть эту сумму из первой зарплаты."
                            ),
                            correctOptionIndex = 1,
                            explanation = "Требование оплаты за трудоустройство – явный признак мошенничества. Легитимные работодатели не взимают плату с кандидатов."
                        ),
                        GameItem(
                            id = 0,
                            scenario = "Вы перешли на страницу входа 'Vkontakte'. URL в адресной строке: 'http://vk.com.auth-page.net/login'. Дизайн страницы идентичен настоящему. Что подозрительно?",
                            options = listOf(
                                "Отсутствие HTTPS (нет 's' в http).",
                                "Странный домен 'auth-page.net' после 'vk.com'.",
                                "Идеальное совпадение дизайна.",
                                "Первые два варианта."
                            ),
                            correctOptionIndex = 3,
                            explanation = "Отсутствие HTTPS и основной домен, не принадлежащий 'vk.com', являются явными признаками фишинговой страницы, даже если дизайн скопирован."
                        ),
                        GameItem(
                            id = 0,
                            scenario = "Страница входа Сбербанк Онлайн запрашивает не только логин и пароль, но и CVC-код вашей карты. Что не так?",
                            options = listOf(
                                "Сбербанк Онлайн никогда не запрашивает CVC-код для входа.",
                                "Это стандартная процедура для повышения безопасности.",
                                "Нужно ввести, если есть сомнения в последних операциях.",
                                "CVC-код нужен только для покупок, а не для входа."
                            ),
                            correctOptionIndex = 0,
                            explanation = "Банки никогда не запрашивают CVC/CVV код карты для входа в личный кабинет. Это явный признак мошенничества."
                        ),
                        GameItem(
                            id = 0,
                            scenario = "Получено письмо: Отправитель: 'Avito Support <support@avito-help.com>', Тема: 'Ваше объявление заблокировано'. Что в первую очередь проверить?",
                            options = listOf(
                                "Тему письма на предмет срочности.",
                                "Точность домена отправителя (avito-help.com вместо avito.ru).",
                                "Наличие логотипа Avito в письме.",
                                "Наличие грамматических ошибок."
                            ),
                            correctOptionIndex = 1,
                            explanation = "Домен отправителя – один из ключевых индикаторов. 'avito-help.com' не является официальным доменом Avito."
                        ),
                        GameItem(
                            id = 0,
                            scenario = "Вы нашли iPhone последней модели на сайте 'super-gadgets.store' на 50% дешевле, чем везде. Сайт новый, отзывов мало. Стоит ли покупать?",
                            options = listOf(
                                "Да, это отличная сделка!",
                                "Нет, слишком низкая цена и мало отзывов – высокий риск мошенничества.",
                                "Попросить продавца прислать фото товара.",
                                "Оплатить наличными при получении, если возможно."
                            ),
                            correctOptionIndex = 1,
                            explanation = "Невероятно низкие цены на новых сайтах с небольшим количеством отзывов – классический признак мошеннических интернет-магазинов."
                        ),
                        GameItem(
                            id = 0,
                            scenario = "При оплате в интернет-магазине вас перенаправляют на страницу, где нужно ввести все данные карты, включая PIN-код. Ваши действия?",
                            options = listOf(
                                "Ввести все данные, включая PIN, для завершения покупки.",
                                "Немедленно покинуть сайт, PIN-код никогда не вводится при онлайн-оплате.",
                                "Проверить, есть ли HTTPS на странице.",
                                "Поискать отзывы об этом магазине."
                            ),
                            correctOptionIndex = 1,
                            explanation = "PIN-код от банковской карты никогда не используется для онлайн-платежей. Запрос PIN-кода – 100% мошенничество."
                        ),
                        GameItem(
                            id = 0,
                            scenario = "Звонок с незнакомого номера: 'Здравствуйте, это служба безопасности банка. Обнаружена подозрительная операция. Назовите номер вашей карты и код из SMS для отмены.' Что делать?",
                            options = listOf(
                                "Продиктовать все данные, чтобы защитить свои деньги.",
                                "Положить трубку и самому перезвонить в банк по официальному номеру.",
                                "Спросить ФИО звонящего и его должность.",
                                "Отказаться называть код из SMS, но номер карты можно."
                            ),
                            correctOptionIndex = 1,
                            explanation = "Никогда не сообщайте полные данные карты, коды из SMS или пароли по телефону. Сотрудники банка никогда этого не попросят. Лучше перезвонить в банк самостоятельно."
                        ),
                        GameItem(
                            id = 0,
                            scenario = "Сообщение в Telegram от имени начальника: 'Привет, я на совещании, не могу говорить. Срочно купи подарочные карты AppStore на 5000р и пришли мне коды.' Как реагировать?",
                            options = listOf(
                                "Срочно выполнить просьбу начальника.",
                                "Ответить, что сейчас нет возможности.",
                                "Связаться с начальником по другому каналу (звонок, SMS) для подтверждения.",
                                "Предложить перевести деньги ему на карту."
                            ),
                            correctOptionIndex = 2,
                            explanation = "Такие просьбы, особенно с указанием конкретных действий (купить карты) и невозможностью говорить, часто являются мошенничеством с использованием взломанных или поддельных аккаунтов."
                        ),
                        GameItem(
                            id = 0,
                            scenario = "Вы в кафе и подключились к бесплатному Wi-Fi 'Cafe_Free_WiFi'. Можно ли безопасно входить в интернет-банк?",
                            options = listOf(
                                "Да, если сайт банка использует HTTPS.",
                                "Нет, публичные Wi-Fi сети могут быть небезопасны для финансовых операций.",
                                "Да, если антивирус на устройстве обновлен.",
                                "Только если скорость соединения хорошая."
                            ),
                            correctOptionIndex = 1,
                            explanation = "Публичные Wi-Fi сети могут быть подвержены перехвату данных. Для финансовых операций лучше использовать мобильный интернет или доверенную домашнюю/рабочую сеть."
                        ),
                        GameItem(
                            id = 0,
                            scenario = "Какой тип шифрования наиболее безопасен для вашей домашней Wi-Fi сети?",
                            options = listOf(
                                "WEP",
                                "WPA",
                                "WPA2 или WPA3",
                                "Открытая сеть без пароля для удобства гостей."
                            ),
                            correctOptionIndex = 2,
                            explanation = "WPA2, а в идеале WPA3, обеспечивают наиболее надежное шифрование для домашних Wi-Fi сетей. WEP и WPA устарели и уязвимы."
                        ),
                         GameItem(
                            id = 0,
                            scenario = "Вы видите две Wi-Fi сети: 'MyHomeWiFi' (защищена паролем) и 'Free_Internet_Access' (открытая). Какую выбрать для просмотра почты с конфиденциальной информацией?",
                            options = listOf(
                                "'Free_Internet_Access', так как не нужно вводить пароль.",
                                "'MyHomeWiFi', так как она защищена паролем.",
                                "Любую, если включен VPN.",
                                "Ту, у которой сигнал сильнее."
                            ),
                            correctOptionIndex = 1,
                            explanation = "Всегда выбирайте защищенную паролем сеть, особенно для доступа к конфиденциальной информации. Открытые сети не обеспечивают шифрования трафика."
                        )
                    )
                    gameItemDao.insertAllGameItems(gameItems)

                    val fakeLoginGameItemDao = database.fakeLoginGameItemDao()
                    fakeLoginGameItemDao.deleteAllItems()
                    val fakeLoginItems = listOf(
                        FakeLoginGameItem(
                            serviceName = "ВКонтакте",
                            description = "URL: vk.com-login-page.net, поля для ввода логина и пароля, кнопка 'Войти'. Дизайн идентичен официальному.",
                            isFake = true,
                            elementsToSpot = listOf("Неверный URL (vk.com-login-page.net вместо vk.com)", "Отсутствие HTTPS, если применимо к скриншоту/описанию"),
                            explanation = "Домен 'vk.com-login-page.net' не является официальным доменом ВКонтакте. Это фишинговый сайт."
                        ),
                        FakeLoginGameItem(
                            serviceName = "Сбербанк Онлайн",
                            description = "URL: https://online.sberbank.ru, защищенное соединение (замочек), поля для логина, пароля, кнопка 'Войти'. Присутствует официальный логотип.",
                            isFake = false,
                            elementsToSpot = listOf(),
                            explanation = "URL 'online.sberbank.ru' с HTTPS является официальным адресом Сбербанк Онлайн. Признаков подделки нет."
                        ),
                        FakeLoginGameItem(
                            serviceName = "Госуслуги",
                            description = "URL: http://gosuslugi-profile.ru, поля для ввода телефона/почты/СНИЛС и пароля. Есть логотип Госуслуг.",
                            isFake = true,
                            elementsToSpot = listOf("Отсутствие HTTPS (используется http)", "Неофициальный URL (gosuslugi-profile.ru вместо gosuslugi.ru)"),
                            explanation = "Отсутствие HTTPS и использование неофициального домена 'gosuslugi-profile.ru' указывают на фишинговый сайт."
                        ),
                        FakeLoginGameItem(
                            serviceName = "Одноклассники",
                            description = "URL: https://ok.ru, яркий дизайн, поля для логина и пароля. Запрос на разрешение доступа к контактам перед входом.",
                            isFake = false,
                            elementsToSpot = listOf(),
                            explanation = "URL 'ok.ru' с HTTPS является официальным. Запрос разрешений может быть стандартным, если это первое использование или новое приложение."
                        ),
                        FakeLoginGameItem(
                            serviceName = "Avito",
                            description = "URL: https://avito.payment-secure.com/pay?order_id=12345. Страница запрашивает полные данные карты для 'безопасной сделки'.",
                            isFake = true,
                            elementsToSpot = listOf("Подозрительный домен 'payment-secure.com'", "Avito обычно проводит сделки через свою платформу, а не сторонние платежные шлюзы с таким URL"),
                            explanation = "Домен 'avito.payment-secure.com' не является официальным платежным шлюзом Avito для всех операций. Будьте осторожны при переходе на внешние ссылки для оплаты."
                        )
                    )
                    fakeLoginGameItemDao.insertAll(fakeLoginItems)
                }
            }
        }
    }
}
