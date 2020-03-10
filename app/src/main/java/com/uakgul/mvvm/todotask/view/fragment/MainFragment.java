package com.uakgul.mvvm.todotask.view.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.uakgul.mvvm.todotask.R;
import com.uakgul.mvvm.todotask.adapter.TaskAdapter;
import com.uakgul.mvvm.todotask.repository.model.Task;
import com.uakgul.mvvm.todotask.util.ArrayListUtils;
import com.uakgul.mvvm.todotask.util.DateUtils;
import com.uakgul.mvvm.todotask.util.PrefOperations;
import com.uakgul.mvvm.todotask.util.Utils;
import com.uakgul.mvvm.todotask.viewmodel.MainViewModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class MainFragment extends Fragment implements View.OnClickListener {

    //region initializeVariables
    private static final String ARG_PARAM1 = "username";
    private static final String ARG_PARAM2 = "password";
    private static final String ARG_PARAM3 = "fullname";

    private static String TAG = "Main";

    Context appContext;

    PrefOperations pref;
    Utils utils;

    SwipeRefreshLayout swipeLayout;
    RecyclerView taskList;
    TaskAdapter adapter;

    SearchView searchView;

    private int userId;
    private String username;
    private String password;
    private String fullname;

    TextView tvNameSurname;


    FloatingActionButton fabAddTask;
    Button btnUpdate;
    Button btnOut;

    RelativeLayout rLContent;

    RelativeLayout rLFilter;
    RelativeLayout rlSort;
    RelativeLayout rlExport;

    InputMethodManager imm;

    private MainViewModel viewModel;
    //endregion

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        appContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_PARAM1);
            password = getArguments().getString(ARG_PARAM2);
            fullname = getArguments().getString(ARG_PARAM3);
        }

        hideKeyboard();
        setStrictMode();

        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_loginFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.initialize(appContext);

        initializeVariables(view);

        getUserAndTasksFromParams();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //Clear query
                searchView.setQuery(null, false);
                //Collapse the action view
                searchView.onActionViewCollapsed();
                searchView.clearFocus();
                hideKeyboard();
                return true;
            }
        });


        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if( swipeLayout.isRefreshing() ) {
                            swipeLayout.setRefreshing(false);
                            showTasks();
                        }

                    }
                }, 1000);
            }
        });

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }//end of onCreateView

    private void initializeVariables(View view){

        pref = new PrefOperations( appContext );

        utils = new Utils( appContext );

        fabAddTask = view.findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(this);

        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

        btnOut = view.findViewById(R.id.btnOut);
        btnOut.setOnClickListener(this);

        rLContent = view.findViewById(R.id.rLContent);

        rLFilter = view.findViewById(R.id.rlFilter);
        rLFilter.setOnClickListener(this);

        rlSort = view.findViewById(R.id.rlSort);
        rlSort.setOnClickListener(this);

        rlExport = view.findViewById(R.id.rlExport);
        rlExport.setOnClickListener(this);

        tvNameSurname = view.findViewById(R.id.tvNameSurname);

        searchView = view.findViewById(R.id.searchview);

        imm   = (InputMethodManager) appContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        taskList = view.findViewById(R.id.task_list);
        taskList.setLayoutManager( new LinearLayoutManager( appContext ) );

        swipeLayout = view.findViewById(R.id.swiperefreshlayout);

    }//end of initializeVariables


    /**********************************************************************************************/

    private void showTasks() {

        viewModel.getAllTasksFromUsersId( userId );
        if (adapter == null) {
            viewModel.allTasksFomUserIdData.observe( getViewLifecycleOwner() , tasksModel -> {
            if (tasksModel != null) {
                    adapter = new TaskAdapter(appContext, tasksModel, userId);
                    taskList.setAdapter(adapter);
            }else{

                adapter.clearTasks();
                adapter = new TaskAdapter(appContext, tasksModel, userId);
                taskList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            });
        }

    }//end of showTasks


    private void getUserAndTasksFromParams() {

        viewModel.getUserFromNameAndPassword(username,password);
        viewModel.userData.observe( getViewLifecycleOwner() , userModel -> {
            if (userModel != null) {
                userId = userModel.getId();
                showTasks();
                tvNameSurname.setText( ( userModel.getName() + " ( " + viewModel.allTasksFomUserIdData.getValue().size() + " ) " ).toUpperCase() );
                utils.customToastMessage("Welcome back " + fullname, "OK", "S");
            }else{
                utils.customToastMessage("Bir Hata Oluştu! " + username + " Kullanıcıadıyla Bir Kayıt Bulunamadı!", "F", "S" );
            }
        });

    }//end of getUserAndTasksFromParams


    @Override
    public void onClick(View view) {

//        searchView.setQuery(null, false);
        searchView.clearFocus();

        switch ( view.getId() ) {

            case R.id.fabAddTask:
                showNewTaskDialog();
                break;

            case R.id.btnOut:
                pref.clearPreferences();
                utils.customToastMessage("GoogBy " + fullname, "OK", "S");
                Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_loginFragment);
                break;

            case R.id.btnUpdate:
                showUpdateUserDialog(userId,username,password,fullname);
                break;

            case R.id.rlFilter:
                showFilterTaskDialog();
                break;

            case R.id.rlSort:
                showSortTaskDialog();
                break;

            case R.id.rlExport:
                if( viewModel.allTasksFomUserIdData.getValue() == null || viewModel.allTasksFomUserIdData.getValue().size() == 0 ){
                    utils.customToastMessage("There is not exist data to save or share." , "Fail", "S");
                }else{
                    if( isStoragePermissionGranted() ){
                        exportTaskToCSV( (ArrayList<Task>) viewModel.allTasksFomUserIdData.getValue(), rLContent );
                    }
                }
                break;
        }
    }//end of onClick



    /**********************************************************************************************/
    // Dialogs
    /**********************************************************************************************/

    public void showNewTaskDialog(){

        final AlertDialog taskCreateDialog;
        AlertDialog.Builder taskCreateDialogBuilder = new AlertDialog.Builder(appContext);
        View mView = getLayoutInflater().inflate(R.layout.dialog_new_task, null);
        //mView.setBackgroundColor(Color.TRANSPARENT);
        final Task task = new Task();

        final EditText etTitle;
        final EditText etDescr;
        final TextView tvDueDate;
        final CheckBox cbFav;
        final CheckBox cbDone;
        final TextView tvColor;

        final Button btnSave;
        final Button btnCancel;
        final Button btDate;

        etTitle   = mView.findViewById(R.id.etTitle);
        etDescr   = mView.findViewById( R.id.etDescription);
        tvDueDate = mView.findViewById(R.id.tvDueDate);
        btDate = mView.findViewById(R.id.btDate);
        tvColor   = mView.findViewById(R.id.tvColorPicker);
        cbFav     = mView.findViewById( R.id.cbFav);
        cbDone    = mView.findViewById( R.id.cbDone);

        btnSave   = mView.findViewById(R.id.btnSave);
        btnCancel = mView.findViewById(R.id.btnCancel);

        taskCreateDialogBuilder.setView(mView);
        taskCreateDialog = taskCreateDialogBuilder.create();
        taskCreateDialog.getWindow().setWindowAnimations( R.style.DialogAnimation_UpBottom );
        taskCreateDialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
        taskCreateDialog.setCanceledOnTouchOutside(false);
        taskCreateDialog.show();

        taskCreateDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    taskCreateDialog.dismiss();
                }
                return true;
            }
        });

        String colorRandom = utils.getRandomColorHEX();
        tvColor.setText( colorRandom );
        tvColor.setTextColor( Color.parseColor( colorRandom ) );
        task.setColor( String.valueOf( colorRandom ) );


        String createDateTime =  DateUtils.getTimeStamp();
        String createDate     =  createDateTime.substring(0,10);
        String createTime     =  createDateTime.substring(11, createDateTime.length() );

        task.setCreateDateTime( createDateTime );
        task.setCreateDate( createDate );
        task.setCreateTime( createTime );

        String dueDateTime =  DateUtils.getTimeStamp();
        String dueDate     =  dueDateTime.substring(0,10);
        String dueTime     =  dueDateTime.substring(11, dueDateTime.length() );

        task.setDueDateTime( dueDateTime );
        task.setDueDate( dueDate );
        task.setDueTime( dueTime );

        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year        = calendar.get(Calendar.YEAR);
                int month       = calendar.get(Calendar.MONTH);
                int dayOfMonth  = calendar.get(Calendar.DAY_OF_MONTH);

                int hour        = calendar.get(Calendar.HOUR_OF_DAY);
                int min         = calendar.get(Calendar.MINUTE);
                int sec         = calendar.get(Calendar.SECOND);

                final String time =  hour + ":" + min + ":" + sec;

                DatePickerDialog datePickerDialog = new DatePickerDialog( appContext,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                String result = year + "-" + (month + 1) + "-" + day;

                                if( day < 10 ) result = year + "-" + (month + 1) + "-0" + day;

                                tvDueDate.setText( result );
                                task.setDueDate( result );
                                task.setDueTime( time );
                                task.setDueDateTime( result + " " + time );

                            }
                        }, year, month, dayOfMonth);

                datePickerDialog.getDatePicker().setMinDate( DateUtils.getFewDaysAgoMilisecond(20 ) );
                datePickerDialog.getDatePicker().setMaxDate( DateUtils.getFewDaysAgoMilisecond(30 ) );
                datePickerDialog.show();

            }
        });

        cbFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( task.getIsFav() == 0 ){
                    task.setIsFav(1);
                }else{
                    task.setIsFav(0);
                }
            }
        });

        cbDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( task.getIsDone() == 0 ){
                    task.setIsDone(1);
                }else{
                    task.setIsDone(0);
                }
            }
        });



        tvColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String colorRandom = utils.getRandomColorHEX();
                tvColor.setText( String.valueOf( colorRandom ) );
                tvColor.setTextColor( Color.parseColor( colorRandom ) );
                task.setColor( String.valueOf( colorRandom ) );
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskCreateDialog.dismiss();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( etTitle.getText().toString().isEmpty() || etDescr.getText().toString().isEmpty() ){
                    utils.customToastMessage("Please enter required area : Title and Description!" , "Fail", "S");
                }else{

                    task.setTitle( etTitle.getText().toString() );
                    task.setDescription( etDescr.getText().toString() );

                    task.setUserId( userId );

                    viewModel.insertTask(task);

                    adapter.appendTask( task );

                    viewModel.userData.getValue().setTasks( viewModel.userData.getValue().getTasks() + 1 );
                    if( task.getIsDone() == 1 ){
                        viewModel.userData.getValue().setDoneTasks( viewModel.userData.getValue().getDoneTasks() + 1 );
                    }else{
                        viewModel.userData.getValue().setPendingTasks( viewModel.userData.getValue().getPendingTasks() + 1 );
                    }

                    viewModel.updateUser( viewModel.userData.getValue() );

                    tvNameSurname.setText( (  viewModel.userData.getValue().getName() + " ( " +  viewModel.allTasksFomUserIdData.getValue().size() + " ) " ).toUpperCase() );

                    // refresh for delete last added item
                    showTasks();

                    taskCreateDialog.dismiss();
                }

            }
        });


    }//end of showNewTaskDialog


    public void showFilterTaskDialog(){

        final AlertDialog filterTaskDialog;
        AlertDialog.Builder filterTaskDialogBuilder = new AlertDialog.Builder(appContext);
        View mView = getLayoutInflater().inflate(R.layout.dialog_filter_task, null);

        Button btnAll;
        Button btnDone;
        Button btnPending;
        Button btnExpire;
        Button btnNonExpire;
        Button btnFav;
        Button btnCancel;

        btnAll = mView.findViewById(R.id.btnAll);
        btnDone = mView.findViewById( R.id.btnDone);
        btnPending = mView.findViewById(R.id.btnPending);
        btnExpire = mView.findViewById(R.id.btnExpire);
        btnNonExpire = mView.findViewById(R.id.btnNonExpire);
        btnFav = mView.findViewById(R.id.btnFav);
        btnCancel = mView.findViewById(R.id.btnCancel);

        filterTaskDialogBuilder.setView(mView);
        filterTaskDialog = filterTaskDialogBuilder.create();
        filterTaskDialog.getWindow().setWindowAnimations( R.style.DialogAnimation_UpBottom );
        filterTaskDialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
        filterTaskDialog.setCanceledOnTouchOutside(false);
        filterTaskDialog.show();


        filterTaskDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    filterTaskDialog.dismiss();
                }
                return true;
            }
        });


        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.getFilter().filter( "ALL" );
                filterTaskDialog.dismiss();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.getFilter().filter( "DONE" );
                filterTaskDialog.dismiss();
            }
        });
        btnPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.getFilter().filter( "PENDING" );
                filterTaskDialog.dismiss();
            }
        });
        btnExpire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.getFilter().filter( "EXPIRE" );
                filterTaskDialog.dismiss();
            }
        });
        btnNonExpire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.getFilter().filter( "NONEXPIRE" );
                filterTaskDialog.dismiss();
            }
        });
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.getFilter().filter( "FAV" );
                filterTaskDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterTaskDialog.dismiss();
            }
        });

    }//end of showFilterTaskDialog


    public void showSortTaskDialog(){

        final AlertDialog sortTaskDialog;
        AlertDialog.Builder sortTaskDialogBuilder = new AlertDialog.Builder(appContext);
        View mView = getLayoutInflater().inflate(R.layout.dialog_sort_task, null);

        Button btnCreateDateAsc;
        Button btnCreateDateDesc;
        Button btnDueDateAsc;
        Button btnDueDateDesc;
        Button btnIsDoneAsc;
        Button btnIsDoneDesc;
        Button btnTitleAsc;
        Button btnTitleDesc;
        Button btnCancel;

        btnCreateDateAsc = mView.findViewById(R.id.btnCreateDateAsc);
        btnCreateDateDesc = mView.findViewById( R.id.btnCreateDateDesc);
        btnDueDateAsc = mView.findViewById(R.id.btnDueDateAsc);
        btnDueDateDesc = mView.findViewById(R.id.btnDueDateDesc);
        btnIsDoneAsc = mView.findViewById(R.id.btnIsDoneAsc);
        btnIsDoneDesc = mView.findViewById(R.id.btnIsDoneDesc);
        btnTitleAsc = mView.findViewById(R.id.btnTitleAsc);
        btnTitleDesc = mView.findViewById(R.id.btnTitleDesc);
        btnCancel = mView.findViewById(R.id.btnCancel);

        sortTaskDialogBuilder.setView(mView);
        sortTaskDialog = sortTaskDialogBuilder.create();
        sortTaskDialog.getWindow().setWindowAnimations( R.style.DialogAnimation_UpBottom );
        sortTaskDialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
        sortTaskDialog.setCanceledOnTouchOutside(false);
        sortTaskDialog.show();


        sortTaskDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    sortTaskDialog.dismiss();
                }
                return true;
            }
        });

        btnCreateDateAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAdapterSortedList (ArrayListUtils.sortTaskCreateDate((ArrayList<Task>) viewModel.allTasksFomUserIdData.getValue(), "A" ) );
                sortTaskDialog.dismiss();
            }
        });
        btnCreateDateDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAdapterSortedList (ArrayListUtils.sortTaskCreateDate( (ArrayList<Task>) viewModel.allTasksFomUserIdData.getValue(), "D" ) );
                sortTaskDialog.dismiss();
            }
        });
        btnDueDateAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAdapterSortedList (ArrayListUtils.sortTaskDueDate( (ArrayList<Task>) viewModel.allTasksFomUserIdData.getValue(), "A" ) );
                sortTaskDialog.dismiss();
            }
        });
        btnDueDateDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAdapterSortedList (ArrayListUtils.sortTaskDueDate( (ArrayList<Task>) viewModel.allTasksFomUserIdData.getValue(), "D" ) );
                sortTaskDialog.dismiss();
            }
        });
        btnIsDoneAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAdapterSortedList (ArrayListUtils.sortTaskIsDone( (ArrayList<Task>) viewModel.allTasksFomUserIdData.getValue(), "A" ) );
                sortTaskDialog.dismiss();
            }
        });
        btnIsDoneDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAdapterSortedList (ArrayListUtils.sortTaskIsDone( (ArrayList<Task>) viewModel.allTasksFomUserIdData.getValue(), "D" ) );
                sortTaskDialog.dismiss();
            }
        });
        btnTitleAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAdapterSortedList (ArrayListUtils.sortTaskTitle( (ArrayList<Task>) viewModel.allTasksFomUserIdData.getValue(), "A" ) );
                sortTaskDialog.dismiss();
            }
        });
        btnTitleDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAdapterSortedList (ArrayListUtils.sortTaskTitle( (ArrayList<Task>) viewModel.allTasksFomUserIdData.getValue(), "D" ) );
                sortTaskDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortTaskDialog.dismiss();
            }
        });

    }//end of showSortTaskDialog


    /**********************************************************************************************/
    // Sorting Methods
    /**********************************************************************************************/


    private void setAdapterSortedList(ArrayList<Task> taskListToSort) {
        viewModel.allTasksFomUserIdData.setValue( taskListToSort );
        adapter.notifyDataSetChanged();
    }//end of setAdapterSortedList

    /**********************************************************************************************/





    /**********************************************************************************************/

    public void showUpdateUserDialog(final int userId, String usernameOld, String passwordOld, String fullnameOld){

        final AlertDialog updateUserDialog;
        AlertDialog.Builder updateUserDialogBuilder = new AlertDialog.Builder(appContext);
        View mView = getLayoutInflater().inflate(R.layout.dialog_update_user, null);

        final EditText etUserName;
        final EditText etPassword;
        final TextView etFullName;

        final Button btnSave;
        final Button btnCancel;

        etUserName = mView.findViewById(R.id.etUserName);
        etPassword = mView.findViewById( R.id.etPassword);
        etFullName = mView.findViewById(R.id.etNameSurname);

        etUserName.setText( usernameOld );
        etPassword.setText( passwordOld );
        etFullName.setText( fullnameOld );

        btnSave   = mView.findViewById(R.id.btnSave);
        btnCancel = mView.findViewById(R.id.btnCancel);

        updateUserDialogBuilder.setView(mView);
        updateUserDialog = updateUserDialogBuilder.create();
        updateUserDialog.getWindow().setWindowAnimations( R.style.DialogAnimation_UpBottom );
        updateUserDialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
        updateUserDialog.setCanceledOnTouchOutside(false);
        updateUserDialog.show();


        updateUserDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    updateUserDialog.dismiss();
                }
                return true;
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserDialog.dismiss();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( etUserName.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty() || etFullName.getText().toString().isEmpty()){
                    utils.customToastMessage("Please enter required area!" , "Fail", "S");
                }else{

                    viewModel.userData.getValue().setUsername( etUserName.getText().toString() );
                    viewModel.userData.getValue().setPassword( etPassword.getText().toString() );
                    viewModel.userData.getValue().setName( etFullName.getText().toString() );

                    viewModel.updateUser( viewModel.userData.getValue() );

                    if( pref.isAnyRememberUser() ){
                        pref.setUserPreferences( viewModel.userData.getValue().getUsername() , viewModel.userData.getValue().getPassword(), viewModel.userData.getValue().getName() );
                    }

                    tvNameSurname.setText( ( viewModel.userData.getValue().getName() + " ( " + viewModel.allTasksFomUserIdData.getValue().size() + " ) " ).toUpperCase() );

                    updateUserDialog.dismiss();
                }
            }
        });

    }//end of showUpdateUserDialog



    /**********************************************************************************************/
    // Permission Methods
    /**********************************************************************************************/

    @TargetApi(Build.VERSION_CODES.M)
    public  boolean isStoragePermissionGranted() {

        if (utils.getVersionCode() >= Build.VERSION_CODES.M ) {
            if ( ContextCompat.checkSelfPermission( getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED ) {
                return true;
            } else {
                requestPermissions( new String[]{  Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1  );
//                 ActivityCompat.requestPermissions(MainFragment.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }else{
            return true;
        }

    }//end of isStoragePermissionGranted

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.i(TAG, "MainActiviy.onRequestPermissionsResult() permission was  granted, yay!  " );
                    exportTaskToCSV((ArrayList<Task>) viewModel.allTasksFomUserIdData.getValue(),rLContent );
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    showDialogOK("NEEDS WRITE_EXTERNAL_STORAGE PERMSSION FOR SHARE DATA.",
                            new DialogInterface.OnClickListener() {
                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            requestPermissions( new String[]{  Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1  );
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            // proceed with logic by disabling the related features or quit the app.
                                            break;
                                    }
                                }
                            });
                }
                break;
        }
    }//end of onRequestPermissionsResult


    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder( appContext )
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }





    /**********************************************************************************************/
    // Util Methods
    /**********************************************************************************************/

    public void hideKeyboard(){
        if (imm != null) {
            imm.toggleSoftInput( 0 , 0 );
        }
    }//end of hideKeyboard


    public void setStrictMode() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }//end of setStrictMode



    public void exportTaskToCSV(ArrayList<Task> taskList, RelativeLayout rLContent) {
        String CSV_SEPARATOR = ",";
        String errorMsg = "";

        String fileName = taskList.get(0).getUserId() + "_" + DateUtils.getSystemDate() + "_" + DateUtils.getSystemTime() + ".csv";
//        String filePath = appContext.getFilesDir().getPath() + "/" + fileName;    //  /data/user/0/com.uakgul.todotask/files/1_20191006_175649.csv
//        String filePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName; // /storage/emulated/0/Android/data/com.uakgul.todotask/files/Download/1_20191006_180549.csv
        //final String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName; // permission Denied
        final String filePath = Objects.requireNonNull(getActivity()).getApplicationContext().getFileStreamPath(fileName ).getPath();

        Log.e( TAG, "MainFragment.exportTaskToCSV()... filePath : " + filePath );
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.e( TAG, "MainFragment.exportTaskToCSV()... dir      : " + dir );

        final File file = new File( filePath );

        try {
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter( new FileOutputStream( file ), "UTF-8" )
            );

            for (Task task : taskList) {

                StringBuffer oneLine = new StringBuffer();
                oneLine.append( task.getId() );
                oneLine.append( CSV_SEPARATOR );
                oneLine.append( task.getUserId() );
                oneLine.append( CSV_SEPARATOR );
                oneLine.append( task.getDescription() );
                oneLine.append( CSV_SEPARATOR );
                oneLine.append( task.getDueDateTime() );
                oneLine.append( CSV_SEPARATOR );
                oneLine.append( task.getColor() );
                oneLine.append( CSV_SEPARATOR );
                oneLine.append( task.getIsFav() == 0 ? "Not Favorite" : "Favorite" );
                oneLine.append( CSV_SEPARATOR );
                oneLine.append( task.getIsDone() == 0 ? "Pending" : "Done" );
                oneLine.append( CSV_SEPARATOR );
                oneLine.append( task.getCreateDateTime() );

                bw.write( oneLine.toString() );
                bw.newLine();
            }
            bw.flush();
            bw.close();


        } catch (UnsupportedEncodingException e) {
            errorMsg = e.getMessage();
            Log.e( TAG, "MainFragment.exportTaskToCSV()... UnsupportedEncodingException! " + e.getMessage() );
        } catch (FileNotFoundException e) {
            errorMsg = e.getMessage();
            Log.e( TAG, "MainFragment.exportTaskToCSV()... FileNotFoundException! " + e.getMessage() );
        } catch (IOException e) {
            errorMsg = e.getMessage();
            Log.e( TAG, "MainFragment.exportTaskToCSV()... IOException! " + e.getMessage() );
        }

        if( errorMsg.isEmpty() ){

//            utils.customToastMessage("CSV file was created : " + filePath, "OK", "S");

            Snackbar.make( rLContent, "HEADER", Snackbar.LENGTH_LONG)
                    .setText("CSV file was created : " + filePath )
                    .setActionTextColor(getResources().getColor( R.color.blue500 ) )
                    .setAction("OPEN", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent open = new Intent(Intent.ACTION_OPEN_DOCUMENT );
                            open.setType( "text/csv" );
                            open.setData(  Uri.fromFile( new File( filePath ) ) );
                            open.setDataAndType(  Uri.fromFile( file ) , "text/csv"  );
                            //open.setDataAndType(  Uri.fromFile( file ) , appContext.getContentResolver().getType( Uri.fromFile( file ) )  );
                            open.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity( Intent.createChooser( open , "OPEN" ) );
                        }
                    })
                    // TODO fix these
                    //.setAction("SHARE", new View.OnClickListener() {
                    //    @Override
                    //    public void onClick(View view) {
//                  //          utils.customToastMessage("Sorry :/ Uri error", "Fail", "S");
                    //        Intent share = new Intent(Intent.ACTION_SEND);
//                  //          share.setType( "text/csv" );
//                  //          share.setData(  Uri.fromFile( new File(filePath) ) );
                    //        share.setDataAndType(  Uri.fromFile( file ) , "text/csv"  );
                    //        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    //        startActivity( Intent.createChooser( share , "Share With" ) );
                    //    }
                    //})
                    .show();

        }else{
            utils.customToastMessage("Could not save CSV file!! " + errorMsg, "Fail", "L");
        }

    }//end of exportTaskToCSV

}
