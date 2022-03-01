package com.example.timetrackingapp.presentation.timetracking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.timetrackingapp.domain.model.Task
import com.example.timetrackingapp.domain.usecase.AddTaskUseCase
import com.example.timetrackingapp.domain.usecase.GetTasksUseCase
import com.example.timetrackingapp.shared.Response
import com.example.timetrackingapp.shared.toResponseObservable
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class TimeTrackingViewModel {
    interface Input {
        fun loadTasks()
        fun addTask(name: String, desc: String)
        fun startTimer()
        fun stopTimer()
        fun getTasks(): List<Task>
    }

    interface Output {
        val tasks: LiveData<List<TimeTrackingItem>>
        val loading: LiveData<Boolean>
        val errors: LiveData<Throwable>
        val timer: LiveData<Long>
    }

    @HiltViewModel
    class Viewmodel @Inject constructor(
        private val addTaskUseCase: AddTaskUseCase,
        private val getTasksUseCase: GetTasksUseCase,
    ) : ViewModel(), Input, Output {
        val input: Input = this
        val output: Output = this

        override val tasks = MutableLiveData<List<TimeTrackingItem>>()
        override val loading = MutableLiveData<Boolean>()
        override val errors = MutableLiveData<Throwable>()
        override val timer = MutableLiveData(0L)

        private val compositeDisposable = CompositeDisposable()
        private val getTasksSubject: PublishSubject<Unit> = PublishSubject.create()
        private val addTaskSubject: PublishSubject<Task> = PublishSubject.create()

        private var timerRunning = false
        private var timerDisposable: Disposable? = null

        override fun loadTasks() {
            getTasksSubject.onNext(Unit)
        }

        override fun addTask(name: String, desc: String) {
            addTaskSubject.onNext(Task(name, desc, getTime()))
        }

        override fun startTimer() {
            timerDisposable = Observable
                .interval(1000L, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    timer.postValue(getTime() + SECOND)
                }
            timerRunning = true
        }

        override fun stopTimer() {
            if (timerRunning)
                timerDisposable?.dispose()
            timerRunning = false
            timer.postValue(0L)
        }

        override fun getTasks(): List<Task> {
            return tasks.value
                ?.filterIsInstance<TimeTrackingItem.Data>()
                ?.map { it.data }
                ?: emptyList()
        }

        init {
            configureAddTaskSubject()
            configureGetTasksSubject()
        }

        private fun configureAddTaskSubject() {
            compositeDisposable.add(
                addTaskSubject
                    .subscribe { task ->
                        addTaskUseCase(task)
                            .doOnError { errors.postValue(it) }
                            .doOnComplete { loadTasks() }
                            .subscribeOn(Schedulers.io())
                            .subscribe()
                    }
            )
        }

        private fun configureGetTasksSubject() {
            compositeDisposable.add(
                getTasksSubject.subscribe {
                    getTasksUseCase()
                        .toObservable()
                        .toResponseObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { result ->
                            when (result) {
                                is Response.Success -> {
                                    loading.postValue(false)
                                    tasks.postValue(formatTasks(result.data))
                                }
                                is Response.Error -> {
                                    loading.postValue(false)
                                    errors.postValue(result.throwable ?: UNKNOWN_ERROR_THR)
                                }
                                is Response.Loading -> {
                                    loading.postValue(true)
                                }
                            }
                        }
                })
        }

        private fun formatTasks(tasks: List<Task>?): List<TimeTrackingItem> {
            val list = mutableListOf<TimeTrackingItem>()
            list.add(TimeTrackingItem.Header)
            tasks?.map { list.add(TimeTrackingItem.Data(it)) }
            return list
        }

        private fun getTime(): Long {
            return timer.value ?: 0L
        }

        override fun onCleared() {
            super.onCleared()
            compositeDisposable.dispose()
            if (timerRunning)
                timerDisposable?.dispose()
        }
    }

    companion object {
        private const val SECOND = 1000L

        private val UNKNOWN_ERROR_THR = Throwable("Unknown Error")
    }

}
