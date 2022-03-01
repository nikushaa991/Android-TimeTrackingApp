package com.example.timetrackingapp.domain.usecase

import com.example.timetrackingapp.domain.model.Task
import com.example.timetrackingapp.domain.repo.TaskRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: TaskRepository,
) {

    operator fun invoke(): Single<List<Task>> {
        return repository.getTasks()
    }
}