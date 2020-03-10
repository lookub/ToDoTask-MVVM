package com.uakgul.mvvm.todotask.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.uakgul.mvvm.todotask.R;
import com.uakgul.mvvm.todotask.repository.db.AppDatabase;
import com.uakgul.mvvm.todotask.repository.model.User;
import com.uakgul.mvvm.todotask.util.DateUtils;
import com.uakgul.mvvm.todotask.util.Utils;
import com.uakgul.mvvm.todotask.viewmodel.RegisterViewModel;

import java.util.List;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    //region initializeVariables
    private static String TAG = "Register";

    Context appContext;

    Utils utils;

    EditText etUserName;
    EditText etPassword;
    EditText etNameSurname;

    Button btnRegister;

    String userName;
    String userPassword;
    String nameSurname;

    private RegisterViewModel viewModel;
    //endregion

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        appContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        viewModel.initialize(appContext);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_loginFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        initializeVariables(view);

        viewModel.getAllUsers();
        viewModel.allUsersData.observe( getViewLifecycleOwner() , usersModel -> {
            if (usersModel == null) {
                utils.customToastMessage( "Incorrect username and password!. Please try again with valid information", "Fail", "S");
            }else{
                Log.d( TAG, "RegisterFragment.onCreate() users : " + usersModel.toString() );
            }
        });

        return view;
    }//end of onCreateView


    private void initializeVariables(View view){

        utils = new Utils( appContext );

        etUserName = view.findViewById(R.id.etUserName);
        etPassword = view.findViewById(R.id.etPassword);
        etNameSurname = view.findViewById(R.id.etNameSurname);

        btnRegister = view.findViewById(R.id.buttonRegister);
        btnRegister.setOnClickListener(this);

    }//end of initializeVariables


    private void createAndRegisterUser(final String userName, final String userPassword, String name) {

        if (userName.isEmpty() || userPassword.isEmpty() || name.isEmpty()  ) {
            utils.customToastMessage("Please Enter username, password and name!", "Fail", "S");
        }else{
            String createDateTime =  DateUtils.getTimeStamp();
            String createDate     =  createDateTime.substring(0,10);
            String createTime     =  createDateTime.substring(11, createDateTime.length() );

            viewModel.getUser( userName, userPassword );
            viewModel.userData.observe( getViewLifecycleOwner() , userModel -> {
                if (userModel == null) {
                    User user = new User(userName, userPassword, name, 0, 0, 0, createDate, createTime, createDateTime);
                    viewModel.insertUser( user );
                    utils.customToastMessage("Registration completed successfully", "OK", "S");
                    clearViews();
                }else{
                    utils.customToastMessage("User named " + userName + " already exists! Please use a different name.", "Fail", "S");
                }
            });

        }

    }//end of createAndRegisterUser


    private void clearViews() {
        etUserName.setText( "" );
        etPassword.setText( "" );
        etNameSurname.setText( "" );
    }

    @Override
    public void onClick(View view) {

        if( view == btnRegister ) {

            userName     = etUserName.getText().toString();
            userPassword = etPassword.getText().toString();
            nameSurname  = etNameSurname.getText().toString();

            if (etUserName.getText() == null || userPassword == null || nameSurname == null ||
                    userName.isEmpty() || userPassword.isEmpty() || nameSurname.isEmpty() ) {
                utils.customToastMessage("Please Enter username, password and name!", "Fail", "S");
            }else {
                createAndRegisterUser( userName, userPassword, nameSurname );
            }

        }

    }

}
