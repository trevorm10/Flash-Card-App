package com.example.myflashcardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
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
        composable("welcome") { FlashCardAppScreen(navController) }
        composable("second") { SecondScreen() } // This is where you add the second screen
    }
}

@Composable
fun FlashCardAppScreen(navController: NavController) {
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
                text = buildAnnotatedString {
                    append("Welcome to ")
                    withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Flash Card App")
                    }
                    append("!")
                },
                fontSize = 24.sp,
                color = Color(0xFF1976D2) // Dark blue text
            )
            Spacer(modifier = Modifier.height(20.dp))
            StartButton(navController)
        }
    }
}

@Composable
fun StartButton(navController: NavController) {
    Text(
        text = "Get Started",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier
            .padding(16.dp)
            .background(Color(0xFF4CAF50)) // Green background
            .clickable { navController.navigate("second") } // Navigate to second screen
            .padding(16.dp) // Padding inside the button
    )
}

// This is the second screen composable
@Composable
fun SecondScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)), // Light blue background
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Lets Get Started!",
            fontSize = 24.sp,
            color = Color(0xFF1976D2) // Dark blue text
        )
    }
}
