package com.uakgul.mvvm.todotask.viewmodel;

import android.content.Context;
import android.util.Log;

import com.uakgul.mvvm.todotask.repository.db.AppDatabase;
import com.uakgul.mvvm.todotask.repository.model.Task;
import com.uakgul.mvvm.todotask.repository.model.User;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {


    public MutableLiveData<List<Task>> allTasksFomUserIdData = new MutableLiveData<List<Task>>();
    public MutableLiveData<Boolean> allTasksLoadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> allTasksLoading = new MutableLiveData<Boolean>();


    public MutableLiveData<Task> taskData = new MutableLiveData<Task>();
    public MutableLiveData<Boolean> taskDataLoadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> taskDataLoading = new MutableLiveData<Boolean>();


    public MutableLiveData<User> userData = new MutableLiveData<User>();
    public MutableLiveData<Boolean> userLoadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> userLoading = new MutableLiveData<Boolean>();


    private AppDatabase appDatabase;

    private Context appContext;

    public void initialize(Context context) {
        appContext = context;
        appDatabase = AppDatabase.getAppDatabase(appContext);
    }


    public void getAllTasksFromUsersId(int userId){
        fetchAllTasks(userId);
    }

    private void fetchAllTasks(int userId){

        allTasksFomUserIdData.setValue( appDatabase.daoTask().getTasksOfAllFromUserId( userId ) );
        if( allTasksFomUserIdData == null ){
            allTasksLoading.setValue(false);
            allTasksLoadError.setValue(true);
        }else{
            allTasksLoading.setValue(false);
            allTasksLoadError.setValue(false);
        }
    }


    public void getUserFromNameAndPassword(String name, String password) {
        userLoading.setValue(true);
        userData.setValue( appDatabase.daoUser().getUserFromNameAndPassword(name,password) );
        if( userData == null ){
            userLoading.setValue(false);
            userLoadError.setValue(true);
        }else{
            userLoading.setValue(false);
            userLoadError.setValue(false);
        }
    }


    public void insertTask(Task task) {
        taskDataLoading.setValue(true);
        Log.d("TAG", "MainViewModel.insertTask... " + task.toString());
        appDatabase.daoTask().insertTask( task );
        taskDataLoading.setValue(false);
        taskData.setValue(task);
        taskDataLoadError.setValue(false);
    }


    public void updateUser(User user) {
        userLoading.setValue(true);
        Log.d("TAG", "MainViewModel.updateUser... " + user.toString());
        appDatabase.daoUser().updateUser( user );
        userLoading.setValue(false);
        userData.setValue(user);
        userLoadError.setValue(false);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
    }



}
