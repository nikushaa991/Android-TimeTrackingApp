package com.example.timetrackingapp.domain.repo

import com.example.timetrackingapp.domain.model.Task
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface TaskRepository {
    fun getTasks(): Single<List<Task>>
    fun addTask(task: Task): Completable
}