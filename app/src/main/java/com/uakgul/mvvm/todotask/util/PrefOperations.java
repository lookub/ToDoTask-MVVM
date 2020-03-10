package com.uakgul.mvvm.todotask.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PrefOperations {

    private static String TAG = "PrefOpr";

    private Context appContext;

    public static String username = "NotDefined";
    public static String password = "NotDefined";
    public static String fullName = "NotDefined";


    public static SharedPreferences.Editor spEditor;
    public static SharedPreferences preferences;

    public static final String myPreferences = "myPref";
    public static final String usernamePref  = "usernamePref";
    public static final String passwordPref  = "passwordPref";
    public static final String fullNamePref  = "fullNamePref";


    public PrefOperations(Context context) {
        appContext = context;
        preferences = appContext.getSharedPreferences( myPreferences, Context.MODE_PRIVATE );
    }

    public void setUserPreferences(String username, String password, String nameSurname){

        Log.i( TAG, "PrefOperations.setUsernamePreferences() :  " + getUsername() + " -> " + username  );
        Log.i( TAG, "PrefOperations.setUsernamePreferences() :  " + getPassword() + " -> " + password  );
        Log.i( TAG, "PrefOperations.setUsernamePreferences() :  " + getFullName() + " -> " + nameSurname  );

        spEditor = preferences.edit();
        spEditor.putString( usernamePref , username );
        spEditor.putString( passwordPref , password );
        spEditor.putString( fullNamePref , nameSurname );
        spEditor.putBoolean("rememberUser", true);
        spEditor.apply();

    }//end of setPreferencesCategory


    public boolean isAnyRememberUser(){

        if( preferences.getBoolean( "rememberUser", Boolean.parseBoolean(null) ) ) {
            return true;
        }else{
            return false;
        }
    }//end of isAnyRememberUser

    public String getPrefUserName(){

        if( isAnyRememberUser() ) {
            String username = preferences.getString( usernamePref,null );
            Log.i( TAG, ".......... getPrefUserName() : preferences.usernamePref : " + username );
            return username;
        }else{
            Log.e( TAG, ".......... getPrefUserName() : preferences.usernamePref NOT SET !!!"  );
            return "";
        }
    }//end of getPrefUserName

    public String getPrefPassword(){

        if( isAnyRememberUser() ) {
            String password = preferences.getString( passwordPref,null );
            Log.i( TAG, ".......... getPrefPassword() : preferences.passwordPref : " + password );
            return password;
        }else{
            Log.e( TAG, ".......... getPrefPassword() : preferences.passwordPref NOT SET !!!"  );
            return "";
        }
    }//end of getPrefPassword

    public String getPrefNameSurname(){

        if( isAnyRememberUser() ) {
            String nameSurname = preferences.getString( fullNamePref,null );
            Log.i( TAG, ".......... getPrefPassword() : preferences.fullNamePref : " + nameSurname );
            return nameSurname;
        }else{
            Log.e( TAG, ".......... getPrefPassword() : preferences.fullNamePref NOT SET !!!"  );
            return "";
        }
    }//end of getPrefNameSurname

    public void clearPreferences() {

        if( isAnyRememberUser()) {
            spEditor = preferences.edit();
            spEditor.putBoolean( "rememberUser", false );
            spEditor.clear();
            spEditor.apply();
        }else{
            Log.e( TAG, ".......... clearPreferences() : there is not have rememberUser!" + getPrefUserName() );
        }

    }//end of clearPreferences


    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        PrefOperations.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        PrefOperations.password = password;
    }

    public static String getFullName() {
        return fullName;
    }

    public static void setFullName(String fullName) {
        PrefOperations.fullName = fullName;
    }

    public static String getMyPreferences() {
        return myPreferences;
    }

    public static String getUsernamePref() {
        return usernamePref;
    }

    public static String getPasswordPref() {
        return passwordPref;
    }

    public static String getFullNamePref() {
        return fullNamePref;
    }
}
