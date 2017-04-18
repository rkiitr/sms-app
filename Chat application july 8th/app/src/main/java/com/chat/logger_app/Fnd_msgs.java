package com.chat.logger_app;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Fnd_msgs extends AppCompatActivity{
    ViewPager vp;
    SwipeAdapter adapter;
    String u2;
    DatabaseHelper db=new DatabaseHelper(this);
    String name="";
    private static final int SELECT_PICTURE = 100;
   // Uri selectedimageuri;
    Bitmap map;
    user_logged_in user;
    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.messages);
        Intent i = getIntent();
        user=new user_logged_in(this);
        u2=i.getExtras().getString("username");
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        vp=(ViewPager)findViewById(R.id.viewpager);
        adapter=new SwipeAdapter(this);
        vp.setAdapter(adapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delete_msg, menu);
        // Log.i(custom, "OnCreateOptionMenu");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if (id == R.id.d_msg) {
            int position=vp.getCurrentItem();
            String table_name=getname(u2, user.getusername());
            int count = db.getmsg_count(user.getusername(), table_name);
            int msg_no=(count-position-1);
      boolean deleted= db.delete_msg(msg_no, table_name);
          //  adapter.destroyItem(3);
         //adapter.destroyItem();
           //adapter.destroyItem(ViewGroup vp.getCurrentItem(), int , Object object);
          adapter.notifyDataSetChanged();
            if(deleted)
            Toast.makeText(Fnd_msgs.this,"Message Deleted",Toast.LENGTH_SHORT).show();
            else
            Toast.makeText(Fnd_msgs.this,"Nothing to Delete",Toast.LENGTH_SHORT).show();
          //  vp.setAdapter(adapter);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {

        finish();

    }
    public String get_user2(){
        return u2;
    }
    public String getname(String user1, String user2){
        name=db.getdatabase_name(user1,user2);
        // Toast.makeText(Fnd_msgs.this,""+name,Toast.LENGTH_SHORT).show();
        return name;
    }
    public void sendmsg(String user1,String user2,String msg,Uri selectedimageuri){
        byte[] inputData =new byte[0];
        try{
            if(selectedimageuri != null ){
                String size = Long.toString(new File(selectedimageuri.getPath()).length());
                // int foo = Integer.parseInt(size);
                InputStream iStream = getContentResolver().openInputStream(selectedimageuri);

                inputData= Utils.getBytes(iStream);
                Bitmap map=Bitmap.createScaledBitmap(Utils.getImage(inputData), 400, 300, true);
                inputData=Utils.getImageBytes(map);
            }

        } catch (IOException ioe) {
            Log.e(TAG, "<saveImageInDB> Error : " + ioe.getLocalizedMessage());
            db.close();

        }
        Date date = new Date();
        // int month=date.getMonth();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        String formattedDate = sdf.format(date);
        db.sendmsg(user1, user2, name, msg,inputData,formattedDate);
        //adapter.notifyDataSetChanged();
        vp.setAdapter(adapter);
    }
     public void showmsg(String user1,String user2){
         Cursor getmsg=db.getmsgs(user1,user2,name);
         String msg_saved="";
         if(getmsg.moveToFirst()){
             do{
                 msg_saved=getmsg.getString(0);
                 break;

             }while(getmsg.moveToNext());

         }
        // Toast.makeText(Fnd_msgs.this,""+msg_saved,Toast.LENGTH_SHORT).show();

     }

  /*  @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPageSelected(int position) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        adapter.notifyDataSetChanged();
    }
*/
   /* public Uri selectImage(){
        openImageChooser();
        return selectedimageuri;

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                selectedimageuri = data.getData();
                try {
                    InputStream iStream=getContentResolver().openInputStream(selectedimageuri);
                    byte[] inputData = Utils.getBytes(iStream);
                    map=Bitmap.createScaledBitmap(Utils.getImage(inputData), 240, 190, true);
                   // btnSelectImage.setImageBitmap(map);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }
    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }*/


}
