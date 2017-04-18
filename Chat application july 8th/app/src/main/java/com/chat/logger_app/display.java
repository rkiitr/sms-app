package com.chat.logger_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.Timer;
import java.util.TimerTask;



public class display extends Activity {
    AppCompatImageView imgView;
    private static final String TAG = "display";
protected void onCreate(Bundle savedInstanceState)
{
    DatabaseHelper dbHelper=new DatabaseHelper(this);
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.display);

    Intent i=getIntent();
    final String username=i.getStringExtra("username");
    //Toast.makeText(getApplication()," "+ username,Toast.LENGTH_SHORT).show();
    imgView = (AppCompatImageView) findViewById(R.id.imgView);
    new Timer().schedule(new TimerTask() {
        public void run() {
            Intent intent=new Intent(display.this,Friends_view.class);
            intent.putExtra("username",username);
            startActivity(intent);
            finish();
          //  overridePendingTransition(R.animator.profpic);
        }
    }, 3000);


    try {


        byte[] bytes = dbHelper.getImage(username);

        imgView.setImageBitmap(Utils.getImage(bytes));
      //  Toast.makeText(this, bytes[10], Toast.LENGTH_SHORT).show();

    } catch (Exception e) {
        Log.e(TAG, "<loadImageFromDB> Error : " + e.getLocalizedMessage());
        dbHelper.close();
    }
Animation animation=AnimationUtils.loadAnimation(this,R.anim.prof_pic);
    imgView.setAnimation(animation);


}


@Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

}
