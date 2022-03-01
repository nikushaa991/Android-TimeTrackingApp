package com.example.timetrackingapp.data.local.roomdb.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

const val TASKS_TABLE = "tasks"

@Entity(tableName = TASKS_TABLE)
data class TaskDTO(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    var desc: String,
    var time: Long,
)
