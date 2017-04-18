package com.chat.logger_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Add_new_fnds extends Activity {
    //AppCompatImageView imgView;
    private static final String TAG = "display";
    DatabaseHelper db=new DatabaseHelper(this);
    //user_logged_in user;
    GridView gv;
    user_logged_in data;
    String current_uname;
    TextView tv;
   ImageAdapter_addnew a;
    private static final long DOUBLE_CLICK_TIME_DELTA = 1000;
    long lastclick=0;
    protected void onCreate(Bundle savedInstanceState)
    {
        DatabaseHelper dbHelper=new DatabaseHelper(this);
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.addnewfnd);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        tv=(TextView)findViewById(R.id.chatting_users);
        Intent i=getIntent();
        //lastclick=0;
        data=new user_logged_in(this);
        current_uname=data.getusername();
        ArrayList<String> al;
        al=db.getnotfriends(data.getusername());
        int count=al.size();
       //Toast.makeText(Add_new_fnds.this,"Size"+count,Toast.LENGTH_SHORT).show();
        if(count==0)
            tv.setText("No More SMS Portal users");
        //user=new user_logged_in(this);
       a= new ImageAdapter_addnew(this.getApplicationContext());
        gv = (GridView) findViewById(R.id.gridview);
        gv.setAdapter(a);
       // Animation animation= AnimationUtils.loadAnimation(this, R.anim.grid_view_anim);
       // gv.setAnimation(animation);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Intent i = new Intent(getApplicationContext(), SingleViewActivity.class);
                long current_click=System.currentTimeMillis();
                if((current_click-lastclick)>DOUBLE_CLICK_TIME_DELTA){
                //gv.setEnabled(false);
                String username = getnames(position);
                //eroor in query
                Cursor c = db.getuserData(username);
                if (c.getCount() == 0) {
                    showMessage("Error", "No Data Entry Found", null);
                    //Toast.makeText(MainActivity.this, "No Data Entry Found", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (c.moveToNext()) {
                    // buffer.append("id :" + c.getString(0) + "\n");
                    buffer.append("Name: " + c.getString(1) + "\n");
                    buffer.append("email: " + c.getString(2) + "\n");
                    buffer.append("uname: " + c.getString(3) + "\n\n");

                }
                    lastclick=current_click;
                showMessage("Friend's Profile", buffer.toString(), username);
                }
                else lastclick=current_click;

            }
        });
        //gv.setEnabled(true);


    }
  /*  @Override
    public void onResume(){
        // put your code here...
        // a.notifyDataChanged();
        gv.setEnabled(true);
        super.onResume();
    }
*/

    public void showMessage(String title, String message,final String username){
        AlertDialog.Builder msg=new AlertDialog.Builder(this);
        msg.setCancelable(true);
        msg.setTitle(title);
        msg.setMessage(message);
        msg.setPositiveButton("Add As Friend", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  Toast.makeText(Add_new_fnds.this,"this is msg",Toast.LENGTH_SHORT)
                //String loggeduser=user.getusername();
                String selecteduser = username;
                db.createfndship(current_uname, selecteduser);
                db.create_fndship_table(current_uname,selecteduser);
                a.notifyDataSetChanged();
                //gv.setAdapter(a);
                Toast.makeText(Add_new_fnds.this, "" + selecteduser + " Added to your Friend list", Toast.LENGTH_SHORT).show();
                // gv.setEnabled(true);
              /*  @Override
                       public void onBackPressed(){
                };*/
                //  String a=db.getdatabase_name();
                // Intent i=new Intent(Add_new_fnds.this,Friends_view.class);
                // startActivity(i);

            }
        });
        msg.show();
    }
    public String getnames(int pos ){
        ArrayList<String> al=new ArrayList<String>();
        al=db.getnotfriends(current_uname);
        String a=al.get(pos);
        return a;


    }
   /* @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.fadeout,R.anim.fadein);
        super.onBackPressed();
    }*/


}
