package com.uakgul.mvvm.todotask.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.uakgul.mvvm.todotask.R;
import com.uakgul.mvvm.todotask.util.PrefOperations;
import com.uakgul.mvvm.todotask.util.Utils;
import com.uakgul.mvvm.todotask.viewmodel.LoginViewModel;


public class LoginFragment extends Fragment implements View.OnClickListener {

    //region initializeVariables
    private static String TAG = "Login";

    Context appContext;

    PrefOperations pref;

    Utils utils;


    EditText etUser;
    EditText etPass;

    Button btnLogin;
    Button btnRegister;

    CheckBox cbRememberMe;

    private String username;
    private String password;
    private String fullname;

    private LoginViewModel viewModel;
    //endregion

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        appContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        viewModel.initialize(appContext);

        initializeVariables(view);

        getRememberedUser(container);

        return view;
    }//end onCreateView


    private void initializeVariables(View view){

        pref = new PrefOperations( appContext );

        utils = new Utils( appContext );

        etUser = view.findViewById(R.id.editTextUserName);
        etPass = view.findViewById(R.id.editTextPassword);

        btnLogin = view.findViewById(R.id.buttonLogin);
        btnRegister = view.findViewById(R.id.buttonRegister);

        cbRememberMe = view.findViewById(R.id.checkBox);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        cbRememberMe.setOnClickListener(this);

    }//end of initializeVariables

    private void getRememberedUser(View container) {

        if( pref.isAnyRememberUser() ){

            username = pref.getPrefUserName();
            password = pref.getPrefPassword();
            fullname = pref.getPrefNameSurname();

            etUser.setText( username );
            etPass.setText( password );
            cbRememberMe.setChecked(true);

            Bundle bundle = new Bundle();
            bundle.putString("username", username );
            bundle.putString("password", password );
            bundle.putString("fullname", fullname );

            Navigation.findNavController(container).navigate(R.id.action_loginFragment_to_mainFragment, bundle );
        }

    }//end of onCreate



    @Override
    public void onClick(View view) {

        if( view == btnLogin ) {

            username = etUser.getText().toString();
            password = etPass.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                utils.customToastMessage("Please enter user information!", "Fail", "S");
            } else {
                validateAndSign( view, username , password );
            }

        }else if( view == btnRegister ) {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
        }else if( view == cbRememberMe ) {
            boolean checked = ((CheckBox) view).isChecked();
            if (checked) {
                utils.customToastMessage("will be remembered " + username + " / " + password, "OK", "S");
            }else{
                utils.customToastMessage("will not be remembered " + username + " / " + password, "Fail", "S");
                pref.clearPreferences();
            }
        }

    }//end of onClick


    private void validateAndSign(View view, final String userName, final String userPassword) {

        viewModel.getUser(userName, userPassword );
        viewModel.userData.observe(this, userModel -> {
            if (userModel == null) {
                utils.customToastMessage( "Incorrect username and password!. Please try again with valid information", "Fail", "S");
            }else{
                if (cbRememberMe.isChecked()) {
                    pref.setUserPreferences( userModel.getUsername() , userModel.getPassword(), userModel.getName() );
                }else{
                    pref.clearPreferences();
                }
                Bundle bundle = new Bundle();
                bundle.putString("username", userModel.getUsername() );
                bundle.putString("password", userModel.getPassword() );
                bundle.putString("fullname", userModel.getName() );
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mainFragment, bundle );
            }
        });

    }//end of validateAndSign

}
