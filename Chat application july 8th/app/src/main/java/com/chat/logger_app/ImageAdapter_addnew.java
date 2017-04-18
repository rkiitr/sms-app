package com.chat.logger_app;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ImageAdapter_addnew extends BaseAdapter {
    user_logged_in data;
    public Context mContext;
    public int count=1;
    //String current_uname;

    // Constructor
    public ImageAdapter_addnew(Context c) {
        mContext = c;
        db=new DatabaseHelper(mContext);
        data=new user_logged_in(mContext);
    }
    DatabaseHelper db;
    //RoundImage roundImage;

    public int getCount() {
        ArrayList<String> al;
        al=db.getnotfriends(data.getusername());
      //  return db.getitemcount();
        return al.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public int getdatacount(){
        return db.getitemcount();

    }
    private String getnames(int pos ){
        ArrayList<String> al=new ArrayList<String>();
        al=db.getnotfriends(data.getusername());
        String a=al.get(pos);
        return a;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ArrayList<String> al;
        al=db.getnotfriends(data.getusername());
        int count=al.size();
        // AppCompatImageView imageView;
        // imageView=new AppCompatImageView(mContext);

        LayoutInflater inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null)
        {
            convertView =inflater.inflate(R.layout.element,parent,false);


        }
        ImageView imageView= (ImageView) convertView.findViewById(R.id.imageView);


        TextView tv= (TextView) convertView.findViewById(R.id.fnd_name);


        if ((position) < count){

            String uname=getnames(position);
            byte[] bytes = db.getImage(uname);
            Cursor data=db.getuserData(uname);
            ArrayList<String> fnames=new ArrayList<String>();
            fnames=db.getFname(uname);
            String fullname=fnames.get(0);


          //  if(bytes != null ){
                Bitmap map = Bitmap.createScaledBitmap(Utils.getImage(bytes),100, 100, true);
                // roundImage=new RoundImage(map);
                //imageView.setImageDrawable(roundImage);
                imageView.setImageBitmap(scaleBitmap(map));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                tv.setText(fullname);

           // }


        }


        return convertView;
        // return imageView;

    }
    private Bitmap scaleBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int maxWidth=200;
        int maxHeight=200;
        int height = bm.getHeight();

        // Log.v("Pictures", "Width and height are " + width + "--" + height);

        if (width > height) {
            // landscape
            int ratio = width / maxWidth;
            width = maxWidth;
            height = height / ratio;
        } else if (height > width) {
            // portrait
            int ratio = height / maxHeight;
            height = maxHeight;
            width = width / ratio;
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }

        // Log.v("Pictures", "after scaling Width and height are " + width + "--" + height);

        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }


}
