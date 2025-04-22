package com.example.myflashcardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashCardAppNavigation()
        }
    }
}

@Composable
fun FlashCardAppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        composable("welcome") { WelcomeScreen(navController) }
        composable("question") { FlashcardQuestionScreen(navController) }
        composable("score/{score}/{answers}") { backStackEntry ->
            val score = backStackEntry.arguments?.getString("score")?.toIntOrNull() ?: 0
            val answers = backStackEntry.arguments?.getString("answers") ?: ""
            ScoreScreen(navController, score, answers)
        }
        composable("review/{answers}") { backStackEntry ->
            val answers = backStackEntry.arguments?.getString("answers") ?: ""
            ReviewScreen(navController, answers)
        }
    }
}

@Composable
fun WelcomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF00796B)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Flashcard Image
            Image(
                painter = painterResource(id = R.drawable.ic_flashcard),
                contentDescription = "Flashcard Icon",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 16.dp),
                colorFilter = ColorFilter.tint(Color(0xFFFFF758))
            )

            Text(
                text = "Welcome to the Flash Card App!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFF758)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "This app will help you gain general knowledge by answering simple questions through a flashcard system. Let's get started!",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            StartButton(navController)
        }
    }
}


@Composable
fun StartButton(navController: NavController) {
    Text(
        text = "Start",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier
            .padding(16.dp)
            .background(Color(0xFFE53935))
            .clickable { navController.navigate("question") }
            .padding(16.dp))
}

@Composable
fun FlashcardQuestionScreen(navController: NavController) {
    val questions = listOf(
        "Nelson Mandela was the president in 1994",
        "The Great Wall of China was built to protect against invasions from the Mongols",
        "The Titanic sank in 1915",
        "The United States declared independence from Great Britain in 1776",
        "The Roman Empire was known for its extensive network of roads",
        "Napoleon Bonaparte was born in Spain"
    )
    val answers = listOf(true, false, true, true, true, false)
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var feedback by remember { mutableStateOf("") }
    var showFeedback by remember { mutableStateOf(false) }
    var userAnswers = remember { mutableStateListOf<Boolean?>(null, null, null, null, null, null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (currentQuestionIndex < questions.size) {
                Text(
                    text = questions[currentQuestionIndex],
                    fontSize = 24.sp,
                    color = Color(0xFF1976D2))

                Spacer(modifier = Modifier.height(20.dp))

                Row {
                    AnswerButton("True") {
                        userAnswers[currentQuestionIndex] = true
                        if (answers[currentQuestionIndex]) {
                            feedback = "Correct!"
                            score++
                        } else {
                            feedback = "Incorrect"
                        }
                        showFeedback = true
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    AnswerButton("False") {
                        userAnswers[currentQuestionIndex] = false
                        if (!answers[currentQuestionIndex]) {
                            feedback = "Correct!"
                            score++
                        } else {
                            feedback = "Incorrect"
                        }
                        showFeedback = true
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                if (showFeedback) {
                    Text(
                        text = feedback,
                        fontSize = 18.sp,
                        color = Color.Black)

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            currentQuestionIndex++
                            showFeedback = false
                        },
                        text = "Next"
                    )
                }
            } else {
                Text(
                    text = "You've completed all questions!",
                    fontSize = 24.sp,
                    color = Color(0xFF1976D2))

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val answersString = userAnswers.joinToString(",") { it?.toString() ?: "null" }
                        navController.navigate("score/$score/$answersString") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    },
                    text = "Finish"
                )
            }
        }
    }
}

@Composable
fun AnswerButton(answer: String, onClick: () -> Unit) {
    Text(
        text = answer,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier
            .padding(16.dp)
            .background(Color(0xFF4CAF50))
            .clickable { onClick() }
            .padding(16.dp))
}

@Composable
fun Button(onClick: () -> Unit, text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier
            .padding(16.dp)
            .background(Color(0xFF4CAF50))
            .clickable { onClick() }
            .padding(16.dp))
}

@Composable
fun ScoreScreen(navController: NavController, score: Int, answers: String) {
    val totalQuestions = 6
    val feedbackMessage = when {
        score == totalQuestions -> "Perfect score! Excellent work!"
        score >= 4 -> "Great job! You're really knowledgeable!"
        score >= 3 -> "Good effort! Keep learning!"
        score >= 2 -> "Not bad, but you can improve!"
        else -> "Keep practicing to get better!"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Your Score: $score / $totalQuestions",
                fontSize = 24.sp,
                color = Color(0xFF1976D2))

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = feedbackMessage,
                fontSize = 18.sp,
                color = Color.Black)

            Spacer(modifier = Modifier.height(20.dp))

            ReviewButton(navController, answers)
        }
    }
}

@Composable
fun ReviewButton(navController: NavController, answers: String) {
    Text(
        text = "Review",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier
            .padding(16.dp)
            .background(Color(0xFF4CAF50))
            .clickable { navController.navigate("review/$answers") }
            .padding(16.dp))
}

@Composable
fun ReviewScreen(navController: NavController, answers: String) {
    val questions = listOf(
        "Nelson Mandela was the president in 1994",
        "The Great Wall of China was built to protect against invasions from the Mongols",
        "The Titanic sank in 1915",
        "The United States declared independence from Great Britain in 1776",
        "The Roman Empire was known for its extensive network of roads",
        "Napoleon Bonaparte was born in Spain"
    )
    val correctAnswers = listOf(true, false, true, true, true, false)
    val userAnswers = answers.split(",").map {
        when(it) {
            "true" -> true
            "false" -> false
            else -> null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Review Answers",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2),
                    modifier = Modifier.align(Alignment.CenterHorizontally))

                Spacer(modifier = Modifier.height(16.dp))

                questions.forEachIndexed { index, question ->
                    val userAnswer = userAnswers.getOrNull(index)
                    val correctAnswer = correctAnswers[index]
                    val isCorrect = userAnswer == correctAnswer

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = if (isCorrect) Color(0xFFC8E6C9) else Color(0xFFFFCDD2),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            text = question,
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Your answer: ${userAnswer?.let { if (it) "True" else "False" } ?: "Skipped"}",
                            color = if (isCorrect) Color(0xFF2E7D32) else Color(0xFFC62828))

                        Text(
                            text = "Correct answer: ${if (correctAnswer) "True" else "False"}",
                            color = Color(0xFF1976D2))
                    }
                }
            }
            ExitButton(navController)
        }
    }
}

@Composable
fun ExitButton(navController: NavController) {
    Text(
        text = "Exit",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier
            .padding(24.dp)
            .background(Color.Red)
            .clickable { navController.navigate("welcome") } // WHEN YOU CLICK EXIT , IT WILL TAKE YOU TO THE WELCOME SCREEN
    )
}