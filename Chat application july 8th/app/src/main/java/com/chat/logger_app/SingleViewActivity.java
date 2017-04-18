package com.chat.logger_app;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class SingleViewActivity extends Activity {
    AppCompatImageView imgView;
    TextView tv;
    DatabaseHelper db=new DatabaseHelper(this);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.single_view);
        imgView=(AppCompatImageView)findViewById(R.id.imgView);
        overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);
       // overridePendingTransition(R.anim.fadein,R.anim.fadeout);
       // overridePendingTransition(R.anim.fndprofin,R.anim.fndprofout);
       // tv=(TextView)findViewById(R.id.msg);
        Intent i = getIntent();
       final String uname = i.getExtras().getString("user");
        byte[] bytes;//=new byte[0];
        bytes = db.getImage(uname);
        Bitmap map= Utils.getImage(bytes);
        //map=blur(Utils.getImage(bytes),10);
        imgView.setImageBitmap(map);
        Animation  animation= AnimationUtils.loadAnimation(this,R.anim.fnds_prof_pic);
       // imgView.setAnimation(animation);
       // Toast.makeText(SingleViewActivity.this,""+uname,Toast.LENGTH_SHORT).show();
        new Timer().schedule(new TimerTask() {
            public void run() {
                Intent i=new Intent(SingleViewActivity.this,Fnd_msgs.class);
                i.putExtra("username",uname);
                startActivity(i);
                finish();
               // startActivity(new Intent(SingleViewActivity.this, Fnd_msgs.class));
                //  overridePendingTransition(R.animator.profpic);
            }
        }, 1000);

        //tv.setText("Message of user N will be displayed here");

    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }


}
