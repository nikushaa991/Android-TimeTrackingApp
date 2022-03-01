package com.example.timetrackingapp.domain.usecase

import com.example.timetrackingapp.domain.model.Task
import com.example.timetrackingapp.domain.repo.TaskRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val repository: TaskRepository,
) {

    operator fun invoke(task: Task): Completable {
        return repository.addTask(task)
    }
}