package com.example.timetrackingapp.shared

fun Long.secondsToTimeText(): String {
    val seconds = (this / 1000) % 60
    val minutes = (this / (1000 * 60) % 60)
    val hours = (this / (1000 * 60 * 60))
    return "${hours.toString().padStart(2, '0')}:${
        minutes.toString().padStart(2, '0')
    }:${seconds.toString().padStart(2, '0')}"
}