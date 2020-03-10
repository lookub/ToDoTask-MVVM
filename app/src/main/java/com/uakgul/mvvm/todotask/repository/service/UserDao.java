package com.uakgul.mvvm.todotask.repository.service;

import com.uakgul.mvvm.todotask.repository.model.User;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {

    // ____________________________________________
    // insert
    //_____________________________________________
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(User... users);

    // ____________________________________________
    // update
    //_____________________________________________
    @Update
    void updateUser(User user);

    @Update
    void updateUsers(User... users);

    // ____________________________________________
    // delete
    //_____________________________________________
    @Query("DELETE FROM User WHERE id = :id ")
    void deleteTaskFromId(int id);

    // ____________________________________________
    // queries
    //_____________________________________________
    @Query("SELECT * FROM User")
    List<User> getAllUser();

    @Query("SELECT * FROM User WHERE id = :id LIMIT 1")
    User getUserFromId(int id);

    @Query("SELECT * FROM User WHERE  username = :username AND password = :password LIMIT 1")
    User getUserFromNameAndPassword(String username, String password);

}
