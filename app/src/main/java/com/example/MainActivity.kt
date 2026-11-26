package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ai.configureAppCheck
import com.example.ui.MiaViewModel
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LessonsScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.theme.MiaPink
import com.example.ui.theme.MiaPurple
import com.example.ui.theme.MiaPurpleContainer
import com.example.ui.theme.MiaTalksTheme

const val ROUTE_HOME = "home"
const val ROUTE_CHAT = "chat"
const val ROUTE_LESSONS = "lessons"
const val ROUTE_QUIZ = "quiz"

private data class TopLevelTab(
    val route: String,
    val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

private val topLevelTabs = listOf(
    TopLevelTab(
        route = ROUTE_HOME,
        labelRes = R.string.nav_home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        testTag = "nav_home"
    ),
    TopLevelTab(
        route = ROUTE_CHAT,
        labelRes = R.string.nav_chat,
        selectedIcon = Icons.AutoMirrored.Filled.Chat,
        unselectedIcon = Icons.AutoMirrored.Outlined.Chat,
        testTag = "nav_chat"
    ),
    TopLevelTab(
        route = ROUTE_LESSONS,
        labelRes = R.string.nav_lessons,
        selectedIcon = Icons.Filled.MenuBook,
        unselectedIcon = Icons.Outlined.MenuBook,
        testTag = "nav_lessons"
    ),
    TopLevelTab(
        route = ROUTE_QUIZ,
        labelRes = R.string.nav_quiz,
        selectedIcon = Icons.Filled.Bolt,
        unselectedIcon = Icons.Outlined.Bolt,
        testTag = "nav_quiz"
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 1: Configure Firebase App Check with launch intent before UI or ViewModels
        configureAppCheck(this, intent)

        enableEdgeToEdge()
        setContent {
            MiaTalksTheme {
                MiaTalksApp()
            }
        }
    }
}

@Composable
fun MiaTalksApp(viewModel: MiaViewModel = viewModel()) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: ROUTE_HOME

    val userStats by viewModel.userStats.collectAsStateWithLifecycle()
    val completedLessonIds by viewModel.completedLessonIds.collectAsStateWithLifecycle()
    val weeklyActivity by viewModel.weeklyActivity.collectAsStateWithLifecycle()
    val activeThreadId by viewModel.activeThreadId.collectAsStateWithLifecycle()
    val activeMessages by viewModel.activeMessages.collectAsStateWithLifecycle()
    val aiState by viewModel.aiState.collectAsStateWithLifecycle()
    val usedPracticeReplyIds by viewModel.usedPracticeReplyIds.collectAsStateWithLifecycle()
    val selectedLessonId by viewModel.selectedLessonId.collectAsStateWithLifecycle()
    val quizState by viewModel.quizState.collectAsStateWithLifecycle()
    val bannerMessage by viewModel.bannerMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val isDark = isSystemInDarkTheme()

    LaunchedEffect(bannerMessage) {
        val msg = bannerMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.clearBannerMessage()
        }
    }

    val navigateToTab: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useNavRail = maxWidth >= 600.dp

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                if (!useNavRail) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        topLevelTabs.forEach { tab ->
                            val selected = currentRoute == tab.route
                            val label = stringResource(tab.labelRes)
                            NavigationBarItem(
                                selected = selected,
                                onClick = { navigateToTab(tab.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = label
                                    )
                                },
                                label = { Text(label) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MiaPurple,
                                    selectedTextColor = MiaPink,
                                    indicatorColor = MiaPurpleContainer
                                ),
                                modifier = Modifier.testTag(tab.testTag)
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (useNavRail) {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        topLevelTabs.forEach { tab ->
                            val selected = currentRoute == tab.route
                            val label = stringResource(tab.labelRes)
                            NavigationRailItem(
                                selected = selected,
                                onClick = { navigateToTab(tab.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = label
                                    )
                                },
                                label = { Text(label) },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = MiaPurple,
                                    selectedTextColor = MiaPink,
                                    indicatorColor = MiaPurpleContainer
                                ),
                                modifier = Modifier.testTag(tab.testTag)
                            )
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = ROUTE_HOME,
                    modifier = Modifier.weight(1f)
                ) {
                    composable(ROUTE_HOME) {
                        HomeScreen(
                            stats = userStats,
                            completedLessonsCount = completedLessonIds.size,
                            weeklyActivity = weeklyActivity,
                            onChatClick = { navigateToTab(ROUTE_CHAT) },
                            onLessonsClick = { navigateToTab(ROUTE_LESSONS) },
                            onQuizClick = { navigateToTab(ROUTE_QUIZ) }
                        )
                    }
                    composable(ROUTE_CHAT) {
                        ChatScreen(
                            activeThreadId = activeThreadId,
                            messages = activeMessages,
                            aiState = aiState,
                            usedPracticeReplyIds = usedPracticeReplyIds,
                            isDarkTheme = isDark,
                            isOnline = viewModel.isOnlineNow(),
                            onSelectThread = viewModel::selectThread,
                            onSendLiveMessage = viewModel::sendLiveChatMessage,
                            onRetryLiveMessage = viewModel::retryLastMessage,
                            onSendOfflinePracticeReply = viewModel::sendOfflinePracticeReply,
                            onClearLiveChat = viewModel::clearLiveChat
                        )
                    }
                    composable(ROUTE_LESSONS) {
                        LessonsScreen(
                            selectedLessonId = selectedLessonId,
                            completedLessonIds = completedLessonIds,
                            onSelectLesson = viewModel::openLesson,
                            onCompleteLesson = viewModel::completeLesson,
                            onPracticeWithMia = { threadId ->
                                viewModel.selectThread(threadId)
                                navigateToTab(ROUTE_CHAT)
                            }
                        )
                    }
                    composable(ROUTE_QUIZ) {
                        QuizScreen(
                            quizState = quizState,
                            onSelectCategory = viewModel::selectQuizCategory,
                            onSelectOption = viewModel::submitQuizAnswer,
                            onNextQuestion = viewModel::nextQuizQuestion,
                            onRestartQuiz = viewModel::restartQuiz,
                            onGoToChat = { navigateToTab(ROUTE_CHAT) }
                        )
                    }
                }
            }
        }
    }
}
