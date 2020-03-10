package com.uakgul.mvvm.todotask.repository.service;

import com.uakgul.mvvm.todotask.repository.model.Task;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TaskDao {

    // ____________________________________________
    // insert
    //_____________________________________________
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Task task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTasks(Task... tasks);

    // ____________________________________________
    // update
    //_____________________________________________
    @Update
    void updateTask(Task task);

    @Update
    void updateTasks(Task... tasks);

    // ____________________________________________
    // delete
    //_____________________________________________
    @Query("DELETE FROM Task WHERE id = :id ")
    void deleteTaskFromId(int id);

    // ____________________________________________
    // queries
    //_____________________________________________
    @Query("SELECT * FROM Task")
    List<Task> getAllTask();

    @Query("SELECT * FROM Task WHERE id = :id LIMIT 1")
    Task getTaskFromId(int id);

    @Query("SELECT * FROM Task WHERE userId = :userId ")
    List<Task> getTasksOfAllFromUserId(int userId);

    @Query("SELECT * FROM Task WHERE userId = :userId AND isDone = 1")
    List<Task> getTasksOfDoneFromUserId(int userId);

    @Query("SELECT * FROM Task WHERE userId = :userId AND isDone = 0")
    List<Task> getTasksOfPendingFromUserId(int userId);

    @Query("SELECT * FROM Task WHERE userId = :userId AND isFav = 1")
    List<Task> getTasksOfFavFromUserId(int userId);

    @Query("SELECT * FROM Task WHERE userId = :userId AND isFav = 0")
    List<Task> getTasksOfNotFavFromUserId(int userId);

    // counts

    @Query("SELECT COUNT(*) AS total FROM Task WHERE userId = :userId ")
    int getTaskCountFromUserId(int userId);

    @Query("SELECT COUNT(*) AS total FROM Task WHERE userId = :userId AND isDone = 1 ")
    int getTaskCountOfDoneFromUserId(int userId);

    @Query("SELECT COUNT(*) AS total FROM Task WHERE userId = :userId AND isDone = 0 ")
    int getTaskCountOfPendingFromUserId(int userId);

    @Query("SELECT COUNT(*) AS total FROM Task WHERE userId = :userId AND isFav = 1 ")
    int getTaskCountOfFavFromUserId(int userId);

    @Query("SELECT COUNT(*) AS total FROM Task WHERE userId = :userId AND isFav = 0 ")
    int getTaskCountOfNotFavFromUserId(int userId);

}
