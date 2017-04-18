package com.chat.logger_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Edit_profile_dialog extends android.support.v4.app.DialogFragment {
    LayoutInflater inflater;
    View view;
    String uname;
    DatabaseHelper dbhelper;
    //EditText cname;
    EditText cpass;
    user_logged_in user;
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
     inflater=getActivity().getLayoutInflater();
        //inflater.inflate(R.layout.edit_prof_dialog,null);
        view=inflater.inflate(R.layout.edit_prof_dialog, null);
        dbhelper=new DatabaseHelper(getActivity());
       // cname=(EditText)
        user=new user_logged_in(getActivity());
        final String username=user.getusername();
        builder.setView(view);
        builder.setTitle("Confirm details");
        builder.create();
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing here because we override this button later to change the close behaviour.
                        //However, we still need this because on older versions of Android unless we
                        //pass a handler the button doesn't get instantiated
                    }
                });
       final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = false;
                //cname = (EditText) view.findViewById(R.id.cusername);
                cpass = (EditText) view.findViewById(R.id.cpass);
                final String mail = user.getemail();
                String name = "custom name";
                // Toast.makeText(getActivity(),"Enter Username " +username,Toast.LENGTH_SHORT).show();
                Cursor data = dbhelper.getAllData();
                while (data.moveToNext()) {
                    String uname = data.getString(3);
                    if (uname.equals(username)) {
                        name = data.getString(1);
                        break;
                    }
                }
                // String username=getusername();
                //Toast.makeText(getActivity(),"Email: "+name,Toast.LENGTH_SHORT).show();
              //  if (username.equals(cname.getText().toString())) {
                   // String str = cname.getText().toString();
                    boolean is_there = false;
               // Toast.makeText(getActivity(),"username: "+username,Toast.LENGTH_SHORT).show();
                    is_there = dbhelper.checkuname(username);
                    String password = cpass.getText().toString();
                    String spassword = dbhelper.searchpass(username);
                     if (password.length() == 0)
                        Toast.makeText(getActivity(), "Enter the Password", Toast.LENGTH_SHORT).show();

                    else if (password.equals(spassword)) {
                        Intent i = new Intent(getActivity(), Edit_prof_form.class);
                        i.putExtra("username", username);
                       wantToCloseDialog=true;
                        startActivity(i);
                       // cname.setText("");
                        cpass.setText("");
                        //   user.setUserLoggedIn(true);
                    } else
                        Toast.makeText(getActivity(), "Wrong password", Toast.LENGTH_SHORT).show();

               // }
               // else
              //    Toast.makeText(getActivity(), "Wrong user name", Toast.LENGTH_SHORT).show();

                if (wantToCloseDialog)
                    dialog.dismiss();

            }
        });

        return dialog;
    }

    public void setusername(String username) {
        uname=username;
    }
    public String getusername(){

        return uname;
    }
}
