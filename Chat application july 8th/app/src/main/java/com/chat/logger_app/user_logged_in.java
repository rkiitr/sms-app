package com.chat.logger_app;

import android.content.Context;
import android.content.SharedPreferences;


public class user_logged_in {
    //Context c;
    public static final String SP_NAME="userDetails";
    SharedPreferences userlocal;
    public user_logged_in(Context c)
    {
    userlocal=c.getSharedPreferences(SP_NAME,0);
    }
    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor=userlocal.edit();
        spEditor.putBoolean("loggedIn",loggedIn);
        spEditor.commit();

    }
public void store_user_data(String u){
    SharedPreferences.Editor spEditor=userlocal.edit();
    spEditor.putString("uname",u);
    spEditor.commit();

}
    public String getusername(){
        String uname=userlocal.getString("uname", "");
        return uname;
    }
    public void store_useremail(String email){
        SharedPreferences.Editor spEditor=userlocal.edit();
        spEditor.putString("email",email);
        spEditor.commit();

    }
    public String getemail(){
        String email=userlocal.getString("email","");
        return email;

    }
    public boolean getuserloggedin(){
        if(userlocal.getBoolean("loggedIn",false)==true)
            return true;
        else return false;

    }

}
