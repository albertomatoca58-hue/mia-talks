package com.example.data

data class ChatThreadMeta(
    val id: String,
    val title: String,
    val emoji: String,
    val isLiveAi: Boolean,
    val subtitle: String
)

data class OfflinePracticeReply(
    val id: String,
    val threadId: String,
    val userPrompt: String,
    val miaReply: String,
    val portugueseNote: String,
    val grammarTip: String? = null
)

data class LessonPhrase(
    val english: String,
    val portuguese: String,
    val pronunciationHint: String,
    val exampleUsage: String
)

data class LessonItem(
    val id: String,
    val title: String,
    val emoji: String,
    val tagline: String,
    val difficulty: String,
    val xpReward: Int,
    val durationMinutes: Int,
    val miaIntro: String,
    val phrases: List<LessonPhrase>,
    val miniDialogueUser: String,
    val miniDialogueMia: String
)

data class QuizQuestion(
    val id: String,
    val category: String,
    val emoji: String,
    val question: String,
    val contextHint: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanationEn: String,
    val explanationPt: String
)

object SampleData {
    const val THREAD_LIVE_AI = "live_mia"
    const val THREAD_GREETINGS = "sample_greetings"
    const val THREAD_SLANG = "sample_slang"
    const val THREAD_MUSIC = "sample_music"
    const val THREAD_SCHOOL = "sample_school"

    const val XP_PER_CORRECT_QUIZ = 10
    const val XP_PER_LESSON = 25
    const val XP_PER_PRACTICE_CHAT = 5

    val chatThreads = listOf(
        ChatThreadMeta(
            id = THREAD_LIVE_AI,
            title = "Live Mia",
            emoji = "✨",
            isLiveAi = true,
            subtitle = "AI Tutor • Needs Internet"
        ),
        ChatThreadMeta(
            id = THREAD_GREETINGS,
            title = "Greetings",
            emoji = "👋",
            isLiveAi = false,
            subtitle = "Offline Sample Chat"
        ),
        ChatThreadMeta(
            id = THREAD_SLANG,
            title = "Slang",
            emoji = "🔥",
            isLiveAi = false,
            subtitle = "Offline Sample Chat"
        ),
        ChatThreadMeta(
            id = THREAD_MUSIC,
            title = "Music",
            emoji = "🎵",
            isLiveAi = false,
            subtitle = "Offline Sample Chat"
        ),
        ChatThreadMeta(
            id = THREAD_SCHOOL,
            title = "School",
            emoji = "🎒",
            isLiveAi = false,
            subtitle = "Offline Sample Chat"
        )
    )

    fun initialChatMessages(baseTime: Long): List<ChatMessageEntity> = listOf(
        // Live AI thread starter
        ChatMessageEntity(
            threadId = THREAD_LIVE_AI,
            sender = "mia",
            text = "Hey! I'm Mia! 👋✨ Welcome to our chat! Tell me about your day in Luanda, ask me cool English slang, or send any English sentence you want to practice!",
            portugueseNote = "Podes escrever como souberes! Se tiveres um errinho, eu corrijo numa boa e explico em português. Bora! 🇦🇴",
            timestampMillis = baseTime,
            isSample = false
        ),

        // Sample 1: Greetings
        ChatMessageEntity(
            threadId = THREAD_GREETINGS,
            sender = "mia",
            text = "Yo! 👋 Imagine you meet your friend outside school. Instead of just 'Hello, how are you?', try saying: 'Hey! What's good?' 😎",
            portugueseNote = "'What's good?' é tipo 'E aí, tudo fixe?' entre amigos!",
            timestampMillis = baseTime + 1_000,
            isSample = true
        ),
        ChatMessageEntity(
            threadId = THREAD_GREETINGS,
            sender = "user",
            text = "Hey Mia! What's good? Yesterday I go to the beach and I am fine!",
            timestampMillis = baseTime + 2_000,
            isSample = true
        ),
        ChatMessageEntity(
            threadId = THREAD_GREETINGS,
            sender = "mia",
            text = "Ayy, love the energy! 🌊✨ Almost perfect! Just two tiny tweaks: for yesterday, say 'I went to the beach' (past tense of go), and 'I'm doing great!' sounds super natural.",
            portugueseNote = "Dica: 'go' no passado vira 'went'! E em vez de 'I am fine', teens falam muito 'I'm doing great!' ou 'I'm chilling!'",
            grammarTip = "'Yesterday I go' → 'Yesterday I went'",
            timestampMillis = baseTime + 3_000,
            isSample = true
        ),

        // Sample 2: Slang
        ChatMessageEntity(
            threadId = THREAD_SLANG,
            sender = "mia",
            text = "Okay, slang class time! 🔥 Today's top 3 words: 'No cap', 'Lowkey', and 'Bet'. Ready?",
            portugueseNote = "São as gírias que mais aparecem no TikTok e nas músicas!",
            timestampMillis = baseTime + 4_000,
            isSample = true
        ),
        ChatMessageEntity(
            threadId = THREAD_SLANG,
            sender = "user",
            text = "Yes! What does 'no cap' mean? Is it about a hat? 🧢",
            timestampMillis = baseTime + 5_000,
            isSample = true
        ),
        ChatMessageEntity(
            threadId = THREAD_SLANG,
            sender = "mia",
            text = "Haha good guess! 😂 'Cap' means lie/fake, so 'No cap!' means 'For real / I'm not lying!' Example: 'Angolan food is the best, no cap!' 🍛🔥",
            portugueseNote = "'No cap' = 'Sem mentira / A sério mesmo!' Tipo 'sem kamba fake'!",
            timestampMillis = baseTime + 6_000,
            isSample = true
        ),

        // Sample 3: Music
        ChatMessageEntity(
            threadId = THREAD_MUSIC,
            sender = "mia",
            text = "Put your headphones on! 🎧🎵 What track is on repeat for you right now? Afrobeats, Kuduro, or Pop?",
            portugueseNote = "'On repeat' significa aquela música que não paras de ouvir!",
            timestampMillis = baseTime + 7_000,
            isSample = true
        ),
        ChatMessageEntity(
            threadId = THREAD_MUSIC,
            sender = "user",
            text = "I love Kuduro! This music have a very good beat!",
            timestampMillis = baseTime + 8_000,
            isSample = true
        ),
        ChatMessageEntity(
            threadId = THREAD_MUSIC,
            sender = "mia",
            text = "Kuduro is iconic!! 🔥💃 Quick grammar boost: with 'this song/music', use 'has' instead of 'have'. Even cooler, say: 'This beat slaps!' 💥",
            portugueseNote = "'This music has...' (terceira pessoa usa 'has'). E 'This beat slaps!' = 'Essa batida bate muito forte / tá braba!'",
            grammarTip = "'This music have' → 'This song has' / 'This beat slaps!'",
            timestampMillis = baseTime + 9_000,
            isSample = true
        ),

        // Sample 4: School
        ChatMessageEntity(
            threadId = THREAD_SCHOOL,
            sender = "mia",
            text = "School mode! 🎒📚 Did your teacher give you a surprise 'pop quiz' this week, or are you chilling?",
            portugueseNote = "'Pop quiz' é aquele teste surpresa que ninguém pediu 😅",
            timestampMillis = baseTime + 10_000,
            isSample = true
        ),
        ChatMessageEntity(
            threadId = THREAD_SCHOOL,
            sender = "user",
            text = "We had a English test! I think I did good!",
            timestampMillis = baseTime + 11_000,
            isSample = true
        ),
        ChatMessageEntity(
            threadId = THREAD_SCHOOL,
            sender = "mia",
            text = "Yaaas, proud of you! 🌟 Tiny grammar polish: use 'an English test' (because English starts with a vowel sound) and 'I did well!' — or teen style: 'I crushed it!' 💪",
            portugueseNote = "Antes de som de vogal usa 'an' ('an English test'). E 'I crushed it!' = 'Eu arrasei!'",
            grammarTip = "'a English test' → 'an English test' • 'did good' → 'did well'",
            timestampMillis = baseTime + 12_000,
            isSample = true
        )
    )

    val offlinePracticeReplies = listOf(
        OfflinePracticeReply(
            id = "rep_greet_1",
            threadId = THREAD_GREETINGS,
            userPrompt = "Long time no see! How have you been?",
            miaReply = "OMG 10/10!! 🙌 You sounded like a native speaker right there! 'Long time no see' is super natural when you bump into a friend after a few weeks.",
            portugueseNote = "'Long time no see' = 'Há quanto tempo!' Continua assim!"
        ),
        OfflinePracticeReply(
            id = "rep_greet_2",
            threadId = THREAD_GREETINGS,
            userPrompt = "Hey, catch you later! I'm heading home.",
            miaReply = "Smooth!! ✨ 'Catch you later' is such a cool, friendly way to say goodbye. You can also say 'See ya!' 👋",
            portugueseNote = "'Catch you later' = 'A gente se vê depois / Até já!'"
        ),
        OfflinePracticeReply(
            id = "rep_slang_1",
            threadId = THREAD_SLANG,
            userPrompt = "I'm lowkey nervous about my presentation.",
            miaReply = "Best use of 'lowkey' ever! 💯 Remember: you've got this! Soon you'll say 'I highkey crushed it!' 🔥",
            portugueseNote = "'Lowkey' = 'um pouco / discretamente'. 'Highkey' = 'muito / sem dúvida!'"
        ),
        OfflinePracticeReply(
            id = "rep_slang_2",
            threadId = THREAD_SLANG,
            userPrompt = "Bet! Let's practice English tomorrow too!",
            miaReply = "Bet!! 🤝✨ Using 'Bet!' to say 'Agreed / Fechado!' is pure teen English mastery!",
            portugueseNote = "'Bet!' = 'Fechado! / Combinado! / Pode apostar!'"
        ),
        OfflinePracticeReply(
            id = "rep_music_1",
            threadId = THREAD_MUSIC,
            userPrompt = "This new Afrobeats track is on repeat!",
            miaReply = "Yesss! 🎶🔥 Send me the vibe! You can also say 'This song is a whole mood!' when it fits your day perfectly.",
            portugueseNote = "'A whole mood' = 'tudo a ver com o momento / uma vibe total!'"
        ),
        OfflinePracticeReply(
            id = "rep_music_2",
            threadId = THREAD_MUSIC,
            userPrompt = "Who is your favorite artist to vibe to?",
            miaReply = "Love how you ended with 'vibe to'! 🎧 That means listening and enjoying the rhythm. Great English!",
            portugueseNote = "'Vibe to' = 'curtir o som de / entrar na vibe de'."
        ),
        OfflinePracticeReply(
            id = "rep_school_1",
            threadId = THREAD_SCHOOL,
            userPrompt = "Want to be study buddies for the math quiz?",
            miaReply = "100% yes! 📝✨ 'Study buddy' is super cute and useful. Together we'll ace that quiz!",
            portugueseNote = "'Study buddy' = 'parceiro(a) de estudo'. 'Ace a quiz' = 'tirar nota máxima!'"
        ),
        OfflinePracticeReply(
            id = "rep_school_2",
            threadId = THREAD_SCHOOL,
            userPrompt = "Let's hang out after class at the courtyard!",
            miaReply = "Love it! 🎒☀️ 'Hang out' is way more natural than 'stay together' when talking about chilling with friends.",
            portugueseNote = "'Hang out' = 'passar tempo juntos / dar um rolê / ficar de boa com a turma'."
        )
    )

    val lessons = listOf(
        LessonItem(
            id = "greetings",
            title = "Greetings",
            emoji = "👋",
            tagline = "Sound natural when meeting your squad",
            difficulty = "Starter",
            xpReward = XP_PER_LESSON,
            durationMinutes = 4,
            miaIntro = "Hey bestie! Ready to upgrade from boring textbook 'How do you do?' to real teen greetings? Let's go! ✨",
            phrases = listOf(
                LessonPhrase(
                    english = "Hey! What's good?",
                    portuguese = "E aí, tudo fixe? / Qual é a boa?",
                    pronunciationHint = "Uóts gúd",
                    exampleUsage = "Hey João! What's good? Ready for football?"
                ),
                LessonPhrase(
                    english = "I'm chilling, how about you?",
                    portuguese = "Estou de boa, e tu?",
                    pronunciationHint = "Aim tchí-lin",
                    exampleUsage = "Nothing much, just chilling at home!"
                ),
                LessonPhrase(
                    english = "Long time no see!",
                    portuguese = "Há quanto tempo! (Saudades!)",
                    pronunciationHint = "Lóng táim nou sí",
                    exampleUsage = "OMG, long time no see! You look awesome!"
                ),
                LessonPhrase(
                    english = "Catch you later!",
                    portuguese = "Até já! / A gente se vê!",
                    pronunciationHint = "Kétch iu lêi-ter",
                    exampleUsage = "Gotta run to class—catch you later!"
                )
            ),
            miniDialogueUser = "Hey Mia! What's good?",
            miniDialogueMia = "Not much, just vibing! Ready to learn? 🚀"
        ),
        LessonItem(
            id = "slang",
            title = "Slang",
            emoji = "🔥",
            tagline = "Master TikTok & teen expressions",
            difficulty = "Fun",
            xpReward = XP_PER_LESSON,
            durationMinutes = 5,
            miaIntro = "If you want to understand YouTube, TikTok, and English memes without translating word-by-word, these slang words are essential! 🔥",
            phrases = listOf(
                LessonPhrase(
                    english = "No cap",
                    portuguese = "Sem mentira / É sério!",
                    pronunciationHint = "Nou kép",
                    exampleUsage = "That sunset at Ilha de Luanda was amazing, no cap!"
                ),
                LessonPhrase(
                    english = "Bet!",
                    portuguese = "Fechado! / Combinado!",
                    pronunciationHint = "Bét",
                    exampleUsage = "Want to study English at 4 PM? — Bet!"
                ),
                LessonPhrase(
                    english = "Lowkey",
                    portuguese = "Um pouco / Meio que / No sigilo",
                    pronunciationHint = "Lou-kí",
                    exampleUsage = "I'm lowkey excited for Friday!"
                ),
                LessonPhrase(
                    english = "You nailed it! / Slay!",
                    portuguese = "Arrasaste! / Mandaste muito bem!",
                    pronunciationHint = "Iu nêild it",
                    exampleUsage = "Your English pronunciation is fire—you nailed it!"
                )
            ),
            miniDialogueUser = "This app is lowkey fun, no cap!",
            miniDialogueMia = "Bet! Wait until you crush the quiz! 😎✨"
        ),
        LessonItem(
            id = "music",
            title = "Music",
            emoji = "🎵",
            tagline = "Talk about Kuduro, Afrobeats & playlists",
            difficulty = "Fun",
            xpReward = XP_PER_LESSON,
            durationMinutes = 5,
            miaIntro = "Music connects everyone! Let's learn how to hype up your favorite songs and artists in English. 🎧💃",
            phrases = listOf(
                LessonPhrase(
                    english = "This beat slaps!",
                    portuguese = "Essa batida bate muito forte / tá braba!",
                    pronunciationHint = "Dis bít sléps",
                    exampleUsage = "Turn up the volume, this Kuduro beat slaps!"
                ),
                LessonPhrase(
                    english = "On repeat",
                    portuguese = "Tocando sem parar (no repeat)",
                    pronunciationHint = "Ón ri-pít",
                    exampleUsage = "I've had that Afrobeats track on repeat all week."
                ),
                LessonPhrase(
                    english = "Drop a track",
                    portuguese = "Lançar uma música nova",
                    pronunciationHint = "Dróp a trék",
                    exampleUsage = "Did you hear? My favorite singer just dropped a track!"
                ),
                LessonPhrase(
                    english = "I know every lyric",
                    portuguese = "Eu sei a letra toda",
                    pronunciationHint = "Ai nou év-ri lí-rik",
                    exampleUsage = "Play it! I know every lyric by heart."
                )
            ),
            miniDialogueUser = "Play that new track, the beat slaps!",
            miniDialogueMia = "Yes!! Put it on repeat! 🔊🔥"
        ),
        LessonItem(
            id = "school",
            title = "School",
            emoji = "🎒",
            tagline = "Classes, tests & chilling with friends",
            difficulty = "Everyday",
            xpReward = XP_PER_LESSON,
            durationMinutes = 5,
            miaIntro = "School life is half studying, half laughing with your friends. Here's how to talk about both in natural English! ✏️✨",
            phrases = listOf(
                LessonPhrase(
                    english = "Pop quiz",
                    portuguese = "Teste surpresa",
                    pronunciationHint = "Póp kuíz",
                    exampleUsage = "Nooo, the teacher gave us a pop quiz!"
                ),
                LessonPhrase(
                    english = "Hang out after school",
                    portuguese = "Ficar de boa / dar um rolê depois da aula",
                    pronunciationHint = "Héng áut",
                    exampleUsage = "Want to hang out after school and grab juice?"
                ),
                LessonPhrase(
                    english = "Study buddy",
                    portuguese = "Colega / parceiro(a) de estudo",
                    pronunciationHint = "Stâ-di bâ-di",
                    exampleUsage = "Need a study buddy for the English exam?"
                ),
                LessonPhrase(
                    english = "I crushed it!",
                    portuguese = "Eu arrasei! / Fui super bem!",
                    pronunciationHint = "Ai krâsht it",
                    exampleUsage = "Got 18/20 on the test—I crushed it!"
                )
            ),
            miniDialogueUser = "I had a pop quiz today, but I crushed it!",
            miniDialogueMia = "Whaaat?! Genius mode! High five! 🙌🎉"
        ),
        LessonItem(
            id = "hangouts",
            title = "Hangouts",
            emoji = "🌅",
            tagline = "Weekend plans, snacks & Luanda vibes",
            difficulty = "Everyday",
            xpReward = XP_PER_LESSON,
            durationMinutes = 4,
            miaIntro = "Weekend is calling! Learn how to invite friends out, order tasty snacks, and describe awesome vibes. 🌴🥤",
            phrases = listOf(
                LessonPhrase(
                    english = "Are you down to go?",
                    portuguese = "Topas ir? / Estás dentro?",
                    pronunciationHint = "Ar iu dáun tu gou",
                    exampleUsage = "We're going for ice cream—are you down?"
                ),
                LessonPhrase(
                    english = "Grab a bite",
                    portuguese = "Comer alguma coisa rápida / lanchar",
                    pronunciationHint = "Gréb a báit",
                    exampleUsage = "I'm starving, let's grab a bite!"
                ),
                LessonPhrase(
                    english = "The view is unreal!",
                    portuguese = "A vista está incrível / surreal!",
                    pronunciationHint = "De viú iz an-ríal",
                    exampleUsage = "Look at the ocean sunset—the view is unreal!"
                ),
                LessonPhrase(
                    english = "Count me in!",
                    portuguese = "Conta comigo!",
                    pronunciationHint = "Káunt mi in",
                    exampleUsage = "Beach football on Saturday? Count me in!"
                )
            ),
            miniDialogueUser = "Are you down to grab a bite after class?",
            miniDialogueMia = "Count me in! Let's get something tasty! 🍔✨"
        )
    )

    val quizQuestions = listOf(
        QuizQuestion(
            id = "q_greet_1",
            category = "Greetings",
            emoji = "👋",
            question = "Your best friend texts: 'What's good?' How should you reply naturally?",
            contextHint = "Casual teen greeting",
            options = listOf(
                "Not much, just chilling! 😎",
                "Yes, the weather is good today.",
                "I am a student in Angola.",
                "Good morning, sir."
            ),
            correctIndex = 0,
            explanationEn = "'What's good?' means 'How are you / What's up?' Replying 'Not much, just chilling!' sounds super natural.",
            explanationPt = "'What's good?' significa 'Tudo fixe / Qual é a boa?' Responder 'Not much, just chilling!' é super natural!"
        ),
        QuizQuestion(
            id = "q_greet_2",
            category = "Greetings",
            emoji = "👋",
            question = "Which phrase means 'Há quanto tempo!' when you see a friend you missed?",
            contextHint = "Reconnecting with a friend",
            options = listOf(
                "Catch you never!",
                "Long time no see!",
                "Short time big look!",
                "Go home slowly!"
            ),
            correctIndex = 1,
            explanationEn = "'Long time no see!' is the classic friendly greeting when you haven't seen someone for a while.",
            explanationPt = "'Long time no see!' significa literalmente 'Há quanto tempo não nos vemos!'"
        ),
        QuizQuestion(
            id = "q_slang_1",
            category = "Slang",
            emoji = "🔥",
            question = "If Mia says: 'That mufete was amazing, no cap!' what does 'no cap' mean?",
            contextHint = "Popular teen slang",
            options = listOf(
                "She forgot her hat",
                "She didn't like the food",
                "For real / I'm not lying!",
                "It was too spicy"
            ),
            correctIndex = 2,
            explanationEn = "'No cap' means 'no lie' or 'for real'!",
            explanationPt = "'No cap' quer dizer 'sem mentira / a sério mesmo'!"
        ),
        QuizQuestion(
            id = "q_slang_2",
            category = "Slang",
            emoji = "🔥",
            question = "Your friend asks: 'Want to practice English at 5 PM?' You agree! What's the coolest one-word reply?",
            contextHint = "Saying 'Fechado / Combinado!'",
            options = listOf(
                "Cap!",
                "Bet!",
                "Quiz!",
                "Sleep!"
            ),
            correctIndex = 1,
            explanationEn = "'Bet!' is teen slang for 'Sure! Agreed! Count me in!'",
            explanationPt = "'Bet!' significa 'Fechado! / Combinado!'"
        ),
        QuizQuestion(
            id = "q_slang_3",
            category = "Slang",
            emoji = "🔥",
            question = "Complete the sentence: 'I'm _____ excited for the weekend!' (meaning 'a little / quietly')",
            contextHint = "Subtle excitement",
            options = listOf(
                "lowkey",
                "loudly",
                "broken",
                "heavy"
            ),
            correctIndex = 0,
            explanationEn = "'Lowkey' means kind of, secretly, or mildly.",
            explanationPt = "'Lowkey' significa 'meio que / um pouco / discretamente'."
        ),
        QuizQuestion(
            id = "q_music_1",
            category = "Music",
            emoji = "🎵",
            question = "You hear an awesome Kuduro beat and want to say it sounds incredible. What do you say?",
            contextHint = "Hyping up a song",
            options = listOf(
                "This beat is sleeping!",
                "This beat slaps!",
                "This beat is homework!",
                "This music have noisy."
            ),
            correctIndex = 1,
            explanationEn = "When a song or beat sounds amazing, teens say 'This beat slaps!'",
            explanationPt = "'This beat slaps!' quer dizer 'Essa batida tá braba / incrível!'"
        ),
        QuizQuestion(
            id = "q_music_2",
            category = "Music",
            emoji = "🎵",
            question = "Fix the grammar: 'This song have a cool vibe.'",
            contextHint = "Watch out for 3rd person singular!",
            options = listOf(
                "This song having a cool vibe.",
                "This song has a cool vibe.",
                "This song are a cool vibe.",
                "This song be a cool vibe."
            ),
            correctIndex = 1,
            explanationEn = "With singular subjects like 'This song', always use 'has' instead of 'have'.",
            explanationPt = "Para 'this song' (singular), usamos 'has' em vez de 'have'!"
        ),
        QuizQuestion(
            id = "q_music_3",
            category = "Music",
            emoji = "🎵",
            question = "What does it mean if a song is 'on repeat'?",
            contextHint = "Listening habits",
            options = listOf(
                "You deleted it from your phone",
                "You play it again and again",
                "You only heard it once",
                "It has no lyrics"
            ),
            correctIndex = 1,
            explanationEn = "If a track is 'on repeat', you can't stop playing it over and over!",
            explanationPt = "'On repeat' significa que estás a ouvir a música sem parar!"
        ),
        QuizQuestion(
            id = "q_school_1",
            category = "School",
            emoji = "🎒",
            question = "Fix this sentence gently like Mia: 'Yesterday I go to school early.'",
            contextHint = "Past tense of 'go'",
            options = listOf(
                "Yesterday I goed to school early.",
                "Yesterday I went to school early.",
                "Yesterday I going to school early.",
                "Yesterday I goes to school early."
            ),
            correctIndex = 1,
            explanationEn = "'Go' is an irregular verb—its past tense is 'went'!",
            explanationPt = "'Go' é irregular: ontem eu fui = 'Yesterday I went'!"
        ),
        QuizQuestion(
            id = "q_school_2",
            category = "School",
            emoji = "🎒",
            question = "What is a 'pop quiz'?",
            contextHint = "Classroom surprise",
            options = listOf(
                "A surprise short test",
                "A music concert at school",
                "A soda bottle",
                "Summer vacation"
            ),
            correctIndex = 0,
            explanationEn = "A 'pop quiz' is an unannounced short test your teacher springs on the class!",
            explanationPt = "'Pop quiz' é um teste surpresa que o professor dá sem avisar!"
        ),
        QuizQuestion(
            id = "q_school_3",
            category = "School",
            emoji = "🎒",
            question = "You got 19/20 on your English test! How do you celebrate in teen English?",
            contextHint = "Proud moment!",
            options = listOf(
                "I crushed it! 💪",
                "I broke my pencil.",
                "I slept in class.",
                "I forgot my backpack."
            ),
            correctIndex = 0,
            explanationEn = "'I crushed it!' means you did an awesome job!",
            explanationPt = "'I crushed it!' significa 'Eu arrasei! / Mandei muito bem!'"
        ),
        QuizQuestion(
            id = "q_school_4",
            category = "School",
            emoji = "🎒",
            question = "Which sentence uses 'a / an' correctly?",
            contextHint = "Vowel sounds take 'an'",
            options = listOf(
                "We have a English class today.",
                "We have an English class today.",
                "We have an math class today.",
                "We have a apple in my bag."
            ),
            correctIndex = 1,
            explanationEn = "Because 'English' starts with a vowel sound, use 'an English class'.",
            explanationPt = "'English' começa com som de vogal, então usamos 'an English class'!"
        )
    )
}
