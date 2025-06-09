package com.maral.quizapp

// Datenklasse für eine einzelne Quizfrage
// questionText: Text der Frage
// options: Antwortmöglichkeiten
// correctAnswerIndex: Index der richtigen Antwort in der Liste
data class Question(
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)
