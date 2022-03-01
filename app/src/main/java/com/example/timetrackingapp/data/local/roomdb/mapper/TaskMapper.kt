package com.example.timetrackingapp.data.local.roomdb.mapper

import com.example.timetrackingapp.data.local.roomdb.dto.TaskDTO
import com.example.timetrackingapp.domain.model.Task

class TaskMapper {
    fun convert(tasks: List<TaskDTO>): List<Task> {
        return tasks.map { Task(it.name, it.desc, it.time) }
    }

    fun convert(task: Task): TaskDTO {
        return TaskDTO(0, task.name, task.desc, task.time)
    }
}