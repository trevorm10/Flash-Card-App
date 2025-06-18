Start (MainActivity)
OnCreate
Call setContent to initialize the app with FlashCardAppNavigation()
FlashCardAppNavigation
Initialize navController using rememberNavController()
Set up NavHost with startDestination as "welcome"
Define navigation routes:
Route: "welcome"
Call WelcomeScreen(navController)
Route: "question"
Call FlashcardQuestionScreen(navController)
Route: "score/{score}/{answers}"
Extract score and answers from backStackEntry
Call ScoreScreen(navController, score, answers)
Route: "review/{answers}"
Extract answers from backStackEntry
Call ReviewScreen(navController, answers)
WelcomeScreen
Create a Box with a background color
Inside the Box, create a Column:
Display Flashcard Image
Display Welcome Message
Display Description Text
Call StartButton(navController)
StartButton
Display "Start" Text
On Click: Navigate to "question"
FlashcardQuestionScreen
Initialize questions and answers lists
Initialize state variables:
currentQuestionIndex, score, feedback, showFeedback, userAnswers
Create a Box with a background color
Inside the Box, create a Column:
If currentQuestionIndex < questions.size:
Display current question
Create a Row with:
AnswerButton("True"):
Update userAnswers, score, and feedback based on user selection
Set showFeedback to true
AnswerButton("False"):
Similar functionality as "True"
If showFeedback is true:
Display feedback
Call Button("Next") to increment currentQuestionIndex
Else (All questions completed):
Display completion message
Call Button("Finish") to navigate to score screen with user answers
AnswerButton
Display answer option (True/False)
On Click: Execute provided onClick function
Button
Display button text
On Click: Execute provided onClick function
ScoreScreen
Calculate totalQuestions and feedbackMessage based on score
Create a Box with a background color
Inside the Box, create a Column:
Display score
Display feedback message
Call ReviewButton(navController, answers)
ReviewButton
Display "Review" Text
On Click: Navigate to "review/{answers}"
ReviewScreen
Initialize questions and correctAnswers lists
Parse userAnswers from answers
Create a Box with a background color
Inside the Box, create a Column:
Display "Review Answers" title
For each question:
Display question
Show user answer and correct answer with appropriate colors
Call ExitButton(navController)
ExitButton
Display "Exit" Text
On Click: Navigate back to "welcome"
