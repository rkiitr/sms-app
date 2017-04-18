package com.chat.logger_app;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Date;


public class Friends_view extends AppCompatActivity {

   // private static final String TAG = "display";
    DatabaseHelper db=new DatabaseHelper(this);
    user_logged_in user;
    String username;
    String database_name;
    GridView gv;
    Fnd_msgs obj;
    ImageAdapter a;
    //int totalfnds;

    SimpleDateFormat dateFormat;
    private static final long DOUBLE_CLICK_TIME_DELTA = 1000;
    long lastclick=0;
  @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //DatabaseHelper dbHelper=new DatabaseHelper(this);
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.friends_view);
        overridePendingTransition(R.anim.fadein, R.anim.zoom_out);
        Intent i=getIntent();
        user=new user_logged_in(this);
        gv = (GridView) findViewById(R.id.gridview);
       a =new ImageAdapter(this.getApplicationContext());
       // gv.setAdapter(new ImageAdapter(this.getApplicationContext()));
        gv.setAdapter(a);
        registerForContextMenu(gv);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Intent i = new Intent(getApplicationContext(), SingleViewActivity.class);
                long current_click=System.currentTimeMillis();
                if((current_click-lastclick)>DOUBLE_CLICK_TIME_DELTA){
                username=getnames(position);
                //eroor in query
                Cursor c=db.getuserData(username);
                if (c.getCount() == 0) {
                    showMessage("Error", "No Data Entry Found",null,null,null);
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
                showMessage("Friend's Profile", buffer.toString(),db.getfullname(username),db.getemail(username),username);
            }
                else lastclick=current_click;
            }
        });



    }
   // @Override
   // public void onResume
   @Override
   public void onResume(){
       super.onResume();

     gv.setAdapter(a);
   }
    ///
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId())
        {
            case R.id.remove_fnds:
                //controller.delete_student(firstname.getText().toString(),lastname.getText().toString());
               // arrayList.remove(info.position);
                String user=getnames(info.position);
                database_name=db.getdatabase_name(user,this.user.getusername());
                db.deletefndship(user, this.user.getusername());
                // Toast.makeText(Friends_view.this,"Table name: "+database_name,Toast.LENGTH_SHORT).show();
               db.delete_fndship_table(database_name);
                a.notifyDataSetChanged();
               // gv.setAdapter(a);
                return  true;

                default:
                return super.onContextItemSelected(item);
        }

    }

    public void showMessage(String title, String message,String name,String email,final String username){

      //String uname=message.toString();
        final   View view=(LayoutInflater.from(Friends_view.this)).inflate(R.layout.friend_prof_dialog,null);
        AlertDialog.Builder confirm=new AlertDialog.Builder(Friends_view.this);
        confirm.setView(view);
        ImageView pic=(ImageView)view.findViewById(R.id.prof_pic_user);
        TextView name_d=(TextView)view.findViewById(R.id.name_d);
       // TextView fnd_count=(TextView)view.findViewById(R.id.fnd_counting);
        TextView uname_tv=(TextView)view.findViewById(R.id.username_d);
       // TextView email_tv=(TextView)view.findViewById(R.id.email_d);
        //ImageButton edit_icon=(ImageButton)view.findViewById(R.id.edit_prof_b);
       // ImageButton del_chat=(ImageButton)view.findViewById(R.id.delete);
       // ImageButton messages=(ImageButton)view.findViewById(R.id.msgs);
       // del_chat.setImageResource(R.drawable.delete_chat);
       // messages.setImageResource(R.drawable.chat);
   /*    del_chat.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String user_name = username;
               database_name = db.getdatabase_name(user_name, user.getusername());
               boolean deleted= db.delete_chat(database_name);
               gv.setAdapter(a);
               if(deleted)
                   Toast.makeText(Friends_view.this,"Chat deleted successfully",Toast.LENGTH_SHORT).show();
               else
                   Toast.makeText(Friends_view.this,"Nothing to delete",Toast.LENGTH_SHORT).show();
           }
       });*/
        confirm.setPositiveButton("See messages", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Friends_view.this, SingleViewActivity.class);
                i.putExtra("user", username);
                db.update_unread(username, db.getdatabase_name(user.getusername(), username));
                //Toast.makeText(Friends_view.this,""+username,Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });
        confirm.setNegativeButton("Delete Chat", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String user_name = username;
                database_name = db.getdatabase_name(user_name, user.getusername());
                boolean deleted= db.delete_chat(database_name);
                gv.setAdapter(a);
                if(deleted)
                    Toast.makeText(Friends_view.this,"Chat deleted successfully",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Friends_view.this,"Nothing to delete",Toast.LENGTH_SHORT).show();
            }
        });
       /* messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Friends_view.this, SingleViewActivity.class);
                i.putExtra("user", username);
                db.update_unread(username, db.getdatabase_name(user.getusername(), username));
                //Toast.makeText(Friends_view.this,""+username,Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });*/
        //edit_icon.setImageResource(R.drawable.edit_button);
        ArrayList<String> al;
        al=db.getFriends(user.getusername());
        String details=username+"\n"+email;
        uname_tv.setText(""+details);
        //email_tv.setText(""+email);
       // fnd_count.setText("Total Friends: "+Integer.toString(al.size()));
       /*  edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Friends_view.this,Edit_prof_form.class);
                startActivity(i);
                finish();
            }
        });*/
        String logged_user=user.getusername();
        final byte[] bytes = db.getImage(username);
        pic.setImageBitmap(Utils.getImage(bytes));
        name_d.setText(name);
        final  AlertDialog dialog = confirm.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                ImageView image = (ImageView) dialog.findViewById(R.id.prof_pic_user);
                Bitmap icon = Utils.getImage(db.getImage(username));
                float imageWidthInPX = (float)image.getWidth();

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                        Math.round(imageWidthInPX * (float)icon.getHeight() / (float)icon.getWidth()));
                image.setLayoutParams(layoutParams);


            }
        });

        dialog.getWindow().setLayout(500, 700);


      /*  AlertDialog.Builder msg=new AlertDialog.Builder(this);
        msg.setCancelable(true);
        msg.setTitle(title);
        msg.setMessage(message);
        msg.setPositiveButton("See messages", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Friends_view.this, SingleViewActivity.class);
                i.putExtra("user", username);
                db.update_unread(username, db.getdatabase_name(user.getusername(), username));
                //Toast.makeText(Friends_view.this,""+username,Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });
        msg.setNegativeButton("Delete_chat", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String user_name = username;
                database_name = db.getdatabase_name(user_name, user.getusername());
              boolean deleted= db.delete_chat(database_name);
                gv.setAdapter(a);
                if(deleted)
                Toast.makeText(Friends_view.this,"Chat deleted successfully",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Friends_view.this,"Nothing to delete",Toast.LENGTH_SHORT).show();
            }
        });

        msg.show();  */
    }
    public String getnames(int pos ){
        ArrayList<String> al=new ArrayList<String>();
        al=db.getFriends(user.getusername());
        String a=al.get(pos);
       // totalfnds=al.size();
        return a;


    }

    @Override
    public void onBackPressed() {
        //Intent i=new Intent(Friends_view.this,MainActivity.class);
      //  startActivity(i);
      //  super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Log.i(custom, "OnCreateOptionMenu");
        return true;
    }
  /*  public void showFragment(View v){

        Edit_profile_dialog eprof=new Edit_profile_dialog();
        FragmentManager manager=getFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.add(R.layout.edit_prof_dialog,eprof,"My activity");
        transaction.commit();
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Edit_profile_dialog edit=new Edit_profile_dialog();
        if (id == R.id.action_settings) {
            Intent i=new Intent(Friends_view.this, MainActivity.class);
            user.setUserLoggedIn(false);
            startActivity(i);
            return true;
        }
        if(id == R.id.edit_profile){
            Intent s=new Intent(Friends_view.this,Edit_prof_form.class);
            startActivity(s);
            return true;

        }
        if(id == R.id.add_fnds){
            Intent i=new Intent(Friends_view.this,Add_new_fnds.class);
            startActivity(i);
            return true;
        }
        if(id == R.id.view_profile){

         final   View view=(LayoutInflater.from(Friends_view.this)).inflate(R.layout.view_profile_dialog,null);
            AlertDialog.Builder confirm=new AlertDialog.Builder(Friends_view.this);
            confirm.setView(view);
            ImageView pic=(ImageView)view.findViewById(R.id.prof_pic_user);
            TextView name_d=(TextView)view.findViewById(R.id.name_d);
            // TextView fnd_count=(TextView)view.findViewById(R.id.fnd_counting);
            TextView uname_tv=(TextView)view.findViewById(R.id.username_d);
            // TextView email_tv=(TextView)view.findViewById(R.id.email_d);
            //ImageButton edit_icon=(ImageButton)view.findViewById(R.id.edit_prof_b);
            // edit_icon.setImageResource(R.drawable.edit_button);
            ArrayList<String> al;
            al=db.getFriends(user.getusername());
            String prof=""+user.getusername()+"\n"+db.getemail(user.getusername())+"\n"+"Total Friends: "+Integer.toString(al.size());
            uname_tv.setText(prof);
           // email_tv.setText(""+db.getemail(user.getusername()));
           // fnd_count.setText("Total Friends: "+Integer.toString(al.size()));
           /* edit_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(Friends_view.this,Edit_prof_form.class);
                    startActivity(i);
                    finish();
                }
            });*/
            confirm.setNeutralButton("Edit Profile", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i=new Intent(Friends_view.this,Edit_prof_form.class);
                    startActivity(i);
                    finish();
                }
            });
            String logged_user=user.getusername();
            final byte[] bytes = db.getImage(logged_user);
           pic.setImageBitmap(Utils.getImage(bytes));
            name_d.setText(db.getfullname(logged_user));
           final  AlertDialog dialog = confirm.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.show();

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface d) {
                    ImageView image = (ImageView) dialog.findViewById(R.id.prof_pic_user);
                    Bitmap icon = Utils.getImage(db.getImage(username));
                    float imageWidthInPX = (float)image.getWidth();

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                            Math.round(imageWidthInPX * (float)icon.getHeight() / (float)icon.getWidth()));
                    image.setLayoutParams(layoutParams);


                }
            });

           dialog.getWindow().setLayout(500, 700);

            return true;

        }

        finish();
        return super.onOptionsItemSelected(item);
    }


}

