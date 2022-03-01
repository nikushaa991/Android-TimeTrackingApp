package com.example.timetrackingapp.data.local.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.timetrackingapp.data.local.roomdb.dto.TASKS_TABLE
import com.example.timetrackingapp.data.local.roomdb.dto.TaskDTO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface TasksDAO {
    @Query("SELECT * FROM $TASKS_TABLE")
    fun getTasks(): Single<List<TaskDTO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: TaskDTO): Completable
}