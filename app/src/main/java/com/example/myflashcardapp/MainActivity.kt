package com.example.myflashcardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    }
}

@Composable
fun WelcomeScreen(navController: NavController) {
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
                text = "Welcome to the Flash Card App!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2) // Dark blue text
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "This app will help you learn through flashcards. Let's get started!",
                fontSize = 16.sp,
                color = Color.Black,
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
            .background(Color(0xFF4CAF50)) // Green background
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
        "Water boils at 100 degrees Celsius"
    )
    val answers = listOf(true, false, true, true, true)
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var feedback by remember { mutableStateOf("") }

    if (currentQuestionIndex >= questions.size) {
        navController.navigate("score") {
            popUpTo("question") { inclusive = true }
        }
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
                text = questions[currentQuestionIndex],
                fontSize = 24.sp,
                color = Color(0xFF1976D2) // Dark blue text
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                AnswerButton("True") {
                    if (answers[currentQuestionIndex]) {
                        feedback = "Correct!"
                        score++
                    } else {
                        feedback = "Incorrect"
                    }
                    currentQuestionIndex++
                }
                Spacer(modifier = Modifier.width(16.dp))
                AnswerButton("False") {
                    if (!answers[currentQuestionIndex]) {
                        feedback = "Correct!"
                        score++
                    } else {
                        feedback = "Incorrect"
                    }
                    currentQuestionIndex++
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = feedback, fontSize = 18.sp, color = Color.Black)
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
fun ScoreScreen(navController: NavController) {
    var score by remember { mutableIntStateOf(0) }
    var totalQuestions by remember { mutableIntStateOf(5) } // Total questions

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
            val feedbackMessage = if (score >= 3) "Great job!" else "Keep practicing!"
            Text(
                text = feedbackMessage,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(20.dp))
            ReviewButton()
            Spacer(modifier = Modifier.height(20.dp))
            ExitButton()
        }
    }
}

@Composable
fun ReviewButton() {
    Text(
        text = "Review",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier
            .padding(16.dp)
            .background(Color(0xFF4CAF50)) // Green background
            .clickable { /* Logic to review flashcards */ }
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
            .clickable { /* Logic to exit the app */ }
            .padding(16.dp) // Padding inside the button
    )
}
