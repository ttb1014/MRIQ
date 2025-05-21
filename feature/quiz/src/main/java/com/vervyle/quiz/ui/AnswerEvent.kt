package com.vervyle.quiz.ui

interface AnswerEvent {
    data object Correct : AnswerEvent

    data object Wrong : AnswerEvent
}