package com.example.timetrackingapp.data.local.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.timetrackingapp.data.local.roomdb.dto.TaskDTO

@Database(entities = [TaskDTO::class], version = 1)
abstract class TasksDatabase : RoomDatabase() {
    abstract fun tasksDAO(): TasksDAO

    companion object {
        const val DB_NAME = "tasks"
    }
}