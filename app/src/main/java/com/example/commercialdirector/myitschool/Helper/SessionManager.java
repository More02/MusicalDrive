package com.example.commercialdirector.myitschool.Helper;


import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;
import android.util.Log;

import com.example.commercialdirector.myitschool.models.User;

public class SessionManager {

    private static final String TAG = SessionManager.class.getSimpleName();
    private static final String SHARED_PREF_NAME = "MyItSchool";
    private static final String KEY_USER_ID = "keyuserid";
    private static final String KEY_USER_NAME = "keyusername";
    private static final String KEY_USER_EMAIL = "keyuseremail";
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "AndroidHiveLogin";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public boolean userLogin(User user) {

        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.apply();
        return true;
    }

    public User getUser() {
        return new User(pref.getInt(KEY_USER_ID, 0), pref.getString(KEY_USER_NAME, null), pref.getString(KEY_USER_EMAIL, null));
    }
}
