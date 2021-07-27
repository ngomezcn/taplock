package app.taplock.sapo;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;

public class GetCredentials {

    private String PREFS_NAME = "app.taplock.auth";
    private String PREF_EMAIL = "email";
    private String PREF_PASSWORD = "password";
    String email;
    String password;

    public String getEmail(Context mContext) {

        //SharedPreferences pref = mContext.getSharedPreferences(PREFS_NAME,mContext.MODE_PRIVATE);
        //email = pref.getString(PREF_PASSWORD, null);

        return email;
    }

    public String getPassword(Context mContext) {

        //SharedPreferences pref = mContext.getSharedPreferences(PREFS_NAME,mContext.MODE_PRIVATE);
        //password = pref.getString(PREF_PASSWORD, null);

        return password;
    }
}
