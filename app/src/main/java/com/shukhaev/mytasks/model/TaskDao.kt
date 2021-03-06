package com.shukhaev.mytasks.model

import androidx.room.*
import com.shukhaev.mytasks.model.SortOrder
import com.shukhaev.mytasks.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    fun getTask(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Task>> =
        when (sortOrder) {
            SortOrder.BY_DATE -> getTaskSortedByDate(query, hideCompleted)
            SortOrder.BY_NAME -> getTaskSortedByName(query, hideCompleted)
        }

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC, name")
    fun getTaskSortedByName(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC, created")
    fun getTaskSortedByDate(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM task_table WHERE completed = 1")
    suspend fun deleteCompletedTasks()
}