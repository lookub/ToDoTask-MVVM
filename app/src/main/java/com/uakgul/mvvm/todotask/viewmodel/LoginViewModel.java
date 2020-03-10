package com.uakgul.mvvm.todotask.viewmodel;

import android.content.Context;

import com.uakgul.mvvm.todotask.repository.db.AppDatabase;
import com.uakgul.mvvm.todotask.repository.model.User;

import java.util.List;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    public MutableLiveData<List<User>> allUsersData = new MutableLiveData<List<User>>();
    public MutableLiveData<Boolean> allUsersLoadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> allUsersLoading = new MutableLiveData<Boolean>();

    public MutableLiveData<User> userData = new MutableLiveData<User>();
    public MutableLiveData<Boolean> userLoadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> userLoading = new MutableLiveData<Boolean>();

    private AppDatabase appDatabase;

    private Context appContext;

    public void initialize(Context context) {
        appContext = context;
        appDatabase = AppDatabase.getAppDatabase(appContext);
    }

    public void getUser(String name, String password){
        getUserFromNameAndPassword(name,password);
    }

    private void getUserFromNameAndPassword(String name, String password) {
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

    public void getAllUsers(){
        fetchAllUsers();
    }

    private void fetchAllUsers() {
        allUsersLoading.setValue(true);

        allUsersData.setValue( appDatabase.daoUser().getAllUser() );
        if( allUsersData == null ){
            allUsersLoading.setValue(false);
            allUsersLoadError.setValue(true);
        }else{
            allUsersLoading.setValue(false);
            allUsersLoadError.setValue(false);
        }
    }


    @Override
    protected void onCleared() {
        super.onCleared();
    }

}
