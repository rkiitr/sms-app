package com.chat.logger_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {
   EditText name, pass;
    Button log, sign, Viewall, delete;
    DatabaseHelper dbhelper=new DatabaseHelper(this);
    user_logged_in user;
    @Override
    public void onStart(){
super.onStart();
        if(authenticate())
        {
            Intent i=new Intent(MainActivity.this,Friends_view.class);
            startActivity(i);

        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name=(EditText)findViewById(R.id.uedit);
        user=new user_logged_in(this);
       // name.setMovementMethod(new ScrollingMovementMethod());
        pass=(EditText)findViewById(R.id.pedit);
        log=(Button)findViewById(R.id.l_button);
        sign=(Button)findViewById(R.id.signup_b);
        Viewall=(Button)findViewById(R.id.button);
        delete=(Button)findViewById(R.id.deleteu);
        onDelete();
        onClick();
        signup();
        viewall();

    }
    private boolean authenticate(){
return user.getuserloggedin();

    }
    public void onDelete(){
        delete.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                       Intent i=new Intent(MainActivity.this, removeuser.class);
                        startActivity(i);
                    }
                }
        );
    }


    public void onClick(){

        log.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String str = name.getText().toString();
                        boolean is_there=false;
                        is_there=dbhelper.checkuname(str);
                        String password = pass.getText().toString();
                        String spassword = dbhelper.searchpass(str);
                        if(str.length()==0)
                            Toast.makeText(MainActivity.this,"Enter Username",Toast.LENGTH_SHORT).show();
                        else if(is_there)
                            Toast.makeText(MainActivity.this,"Invalid Username",Toast.LENGTH_SHORT).show();
                        else if(password.length()==0)
                            Toast.makeText(MainActivity.this, "Enter the Password", Toast.LENGTH_SHORT).show();

                        else   if (password.equals(spassword)) {
                            Intent i = new Intent(MainActivity.this, display.class);
                            i.putExtra("username", str);
                            String email=dbhelper.getemail(str);
                            user.store_useremail(email);
                            startActivity(i);
                            name.setText("");
                            pass.setText("");
                            user.setUserLoggedIn(true);
                            user.store_user_data(str);
                        } else
                            Toast.makeText(MainActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();

                    }
                }
        );
    }

    public void signup(){

        sign.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this, Signup.class);
                        startActivity(i);
                    }
                }
        );
    }
    public void viewall(){

        Viewall.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor result = dbhelper.getAllData();
                        if (result.getCount() == 0) {
                            showMessage("Error", "No Data Entry Found");
                            //Toast.makeText(MainActivity.this, "No Data Entry Found", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while (result.moveToNext()) {
                            buffer.append("id :" + result.getString(0) + "\n");
                            buffer.append("Name: " + result.getString(1) + "\n");
                            buffer.append("email: " + result.getString(2) + "\n");
                            buffer.append("uname: " + result.getString(3) + "\n\n");

                        }
                        showMessage("User data", buffer.toString());
                    }
                }
        );
    }
    public void showMessage(String title, String message){
        AlertDialog.Builder msg=new AlertDialog.Builder(this);
        msg.setCancelable(true);
        msg.setTitle(title);
        msg.setMessage(message);
        msg.show();
    }
  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
