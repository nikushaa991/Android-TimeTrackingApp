package com.example.timetrackingapp.di

import android.content.Context
import androidx.room.Room
import com.example.timetrackingapp.data.local.roomdb.TasksDAO
import com.example.timetrackingapp.data.local.roomdb.TasksDatabase
import com.example.timetrackingapp.data.local.roomdb.TasksDatabase.Companion.DB_NAME
import com.example.timetrackingapp.data.local.roomdb.mapper.TaskMapper
import com.example.timetrackingapp.data.repo.TaskRepositoryImpl
import com.example.timetrackingapp.domain.repo.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideTasksDatabase(@ApplicationContext app: Context): TasksDatabase {
        return Room.databaseBuilder(
            app,
            TasksDatabase::class.java,
            DB_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideTasksDao(database: TasksDatabase): TasksDAO {
        return database.tasksDAO()
    }

    @Singleton
    @Provides
    fun bindsTaskMapper(): TaskMapper {
        return TaskMapper()
    }

}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class TasksDataModule {

    @Binds
    abstract fun bindsTasksRepository(repository: TaskRepositoryImpl): TaskRepository
}