package com.example.timetrackingapp.data.repo

import com.example.timetrackingapp.data.local.roomdb.TasksDAO
import com.example.timetrackingapp.data.local.roomdb.mapper.TaskMapper
import com.example.timetrackingapp.domain.model.Task
import com.example.timetrackingapp.domain.repo.TaskRepository
import io.reactivex.rxjava3.core.*
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val dao: TasksDAO,
    private val mapper: TaskMapper,
) : TaskRepository {

    override fun getTasks(): Single<List<Task>> {
        return dao.getTasks().map { mapper.convert(it) }
    }

    override fun addTask(task: Task): Completable {
        return dao.insert(mapper.convert(task))
    }
}
