package com.example.myflashcardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") { WelcomeScreen(navController) }
        composable("question") { FlashcardQuestionScreen(navController) }
        composable("score") { ScoreScreen(navController) }
        composable("review") { ReviewScreen(navController) }
    }
}

@Composable
fun WelcomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFF5722)), //red background
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome to the Flash Card App!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2) // Dark blue text
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
            .background(Color(0xFF3F51B5)) // blue background
            .clickable { navController.navigate("question") } // Navigate to question screen
            .padding(16.dp) // Padding inside the button
    )
}

@Composable
fun FlashcardQuestionScreen(navController: NavController) {
    val questions = listOf(
        "Nelson Mandela was the president in 1994",
        "The Earth is flat",
        "Kangaroos are native to Australia",
        "The capital of France is Paris",
        "Water boils at 100 degrees Celsius",
        "There are 364 days in a year"
    )
    val answers = listOf(true, false, true, true, true,false)
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var feedback by remember { mutableStateOf("") }
    var showFeedback by remember { mutableStateOf(false) }
    var userAnswers = remember { mutableStateListOf<Boolean?>(null, null, null, null, null,null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)), // Light blue background
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (currentQuestionIndex < questions.size) {
                Text(
                    text = questions[currentQuestionIndex],
                    fontSize = 24.sp,
                    color = Color(0xFF1976D2) // Dark blue text
                )
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
                    Text(text = feedback, fontSize = 18.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            currentQuestionIndex++
                            showFeedback = false
                        },
                        text = "Next Question"
                    )
                }
            } else {
                // Show Finish button after the last question
                Text(text = "You've completed all questions!", fontSize = 24.sp, color = Color(0xFF1976D2))
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("score", score)
                        navController.navigate("score") {
                            popUpTo("question") { inclusive = true }
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
            .background(Color(0xFF4CAF50)) // Green background
            .clickable { onClick() } // Handle answer selection
            .padding(16.dp) // Padding inside the button
    )
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
            .background(Color(0xFF4CAF50)) // Green background
            .clickable { onClick() } // Handle button click
            .padding(16.dp) // Padding inside the button
    )
}

@Composable
fun ScoreScreen(navController: NavController) {
    var score by remember { mutableStateOf(0) }
    var totalQuestions by remember { mutableStateOf(6) } // Total questions
    var feedbackMessage by remember { mutableStateOf("") }

    // Retrieve the score from the previous screen
    LaunchedEffect(Unit) {
        score = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("score") ?: 0
    }

    feedbackMessage = when {
        score == totalQuestions -> "Perfect score! Excellent work!"
        score >= 4 -> "Great job! You're really knowledgeable!"
        score >= 3 -> "Good effort! Keep learning!"
        score >= 2 -> "Not bad, but you can improve!"
        else -> "Keep practicing to get better!"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)), // Light blue background
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Your Score: $score / $totalQuestions",
                fontSize = 24.sp,
                color = Color(0xFF1976D2) // Dark blue text
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = feedbackMessage,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(20.dp))
            ReviewButton(navController)
            Spacer(modifier = Modifier.height(20.dp))
            ExitButton()
        }
    }
}

@Composable
fun ReviewButton(navController: NavController) {
    Text(
        text = "Review",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier
            .padding(16.dp)
            .background(Color(0xFF4CAF50)) // Green background
            .clickable {
                navController.navigate("review") // Navigate to review screen
            }
            .padding(16.dp) // Padding inside the button
    )
}

@Composable
fun ExitButton() {
    Text(
        text = "Exit",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier
            .padding(16.dp)
            .background(Color.Red) // Red background for exit
            .clickable {
                // Logic to exit the app
                // Uncomment the line below to close the app
                // finish()
            }
            .padding(16.dp) // Padding inside the button
    )
}

@Composable
fun ReviewScreen(navController: NavController) {


}