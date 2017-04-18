package com.chat.logger_app;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class send_msgs extends Fragment {
    TextView msg;
    DatabaseHelper db=new DatabaseHelper(getActivity());
    ImageButton send;
    ImageButton sImage;
    Fnd_msgs obj;
    user_logged_in user;
    Uri selectedimageuri;
    private static final int SELECT_PICTURE = 100;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.send_msg,container,false);
       user=new user_logged_in(getActivity());
      msg= (TextView) view.findViewById(R.id.message);
     sImage=(ImageButton)view.findViewById(R.id.send_image);
       obj=(Fnd_msgs) getActivity();

        msg.setFocusable(false);
        msg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                msg.setFocusableInTouchMode(true);

                return false;
            }
        });
        send=(ImageButton)view.findViewById(R.id.send_button);
        send.setOnClickListener(
                new View.OnClickListener(){


                    @Override
                    public void onClick(View v) {
                        String msgstr=msg.getText().toString();
                        int count_spaces=get_spaces(msgstr);
                        boolean b=(selectedimageuri !=null);
                        //Toast.makeText(getActivity(),""+b,Toast.LENGTH_SHORT).show();
                        if((msgstr.length()!=0 && (count_spaces !=msgstr.length()))|| b ){
                        msg.setText("");

                            Date date = new Date();
                           // int month=date.getMonth();
                           SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
                           String formattedDate = sdf.format(date);
                           // formattedDate[1];
                        obj.getname(user.getusername(),obj.get_user2());
                        obj.sendmsg(user.getusername(),obj.get_user2(),msgstr.trim(),selectedimageuri);
                        obj.showmsg(user.getusername(),obj.get_user2());
                           //selectedimageuri=null;
                            sImage.setImageResource(R.drawable.ic_file_upload_white_48dp);
                            selectedimageuri=null;
                           Toast.makeText(getActivity(),"message Sent",Toast.LENGTH_SHORT).show();
                           // Toast.makeText(getActivity(),"current: "+formattedDate,Toast.LENGTH_LONG).show();

                        }
                        else
                            Toast.makeText(getActivity(),"Type the message correctly",Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getActivity(),""+db.getdatabase_name(user.getusername(),obj.get_user2()),Toast.LENGTH_SHORT).show();

                    }
                }
        );
        sImage.setOnClickListener(
                new View.OnClickListener(){


                    @Override
                    public void onClick(View v) {
                        openImageChooser();
                        //Toast.makeText(getActivity(),"Image Added",Toast.LENGTH_SHORT).show();
                        //sImage.setImageBitmap(obj.map);
                    }
                }
        );
        return view;
    }
    public int get_spaces(String msg){

        String word=msg;
        int i = 0,
                spaceCount = 0;

        while( i < word.length() ){
            if( word.charAt(i) == ' ' ) {
                spaceCount++;
            }
            i++;
        }
        return spaceCount;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                selectedimageuri = data.getData();
                try {
                    InputStream iStream=getActivity().getContentResolver().openInputStream(selectedimageuri);
                    byte[] inputData = Utils.getBytes(iStream);
                    Bitmap map=Bitmap.createScaledBitmap(Utils.getImage(inputData), 240, 190, true);
                    sImage.setImageBitmap(map);
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
    }

}
