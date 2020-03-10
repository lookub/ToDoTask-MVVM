package com.uakgul.mvvm.todotask.repository.db;

import android.content.Context;

import com.uakgul.mvvm.todotask.repository.model.Task;
import com.uakgul.mvvm.todotask.repository.model.User;
import com.uakgul.mvvm.todotask.repository.service.TaskDao;
import com.uakgul.mvvm.todotask.repository.service.UserDao;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//@TypeConverters(Converters::class) //converter initialization
@Database(entities = {User.class, Task.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract UserDao daoUser();
    public abstract TaskDao daoTask();

    public static AppDatabase getAppDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder( context.getApplicationContext(), AppDatabase.class, "AppDatabase.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return  INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }
}
