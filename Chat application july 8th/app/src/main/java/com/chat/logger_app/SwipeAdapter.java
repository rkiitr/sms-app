package com.chat.logger_app;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SwipeAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    private Context ctx;
    Fnd_msgs obj;
    user_logged_in user;
    int msg_count;
    SimpleDateFormat dateFormat;
    private static final String TAG = "MainActivity";

    public SwipeAdapter(Context c){
        ctx=c;
        obj=(Fnd_msgs) c;
        helper=new DatabaseHelper(ctx);
        user=new user_logged_in(ctx);
        dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    DatabaseHelper helper;
    @Override
    public int getCount() {
        //helper.getmsgs(obj.u2, obj.get_user2(), obj.getname(obj.u2, obj.get_user2()));
      // return helper.getitemcount();
        String table_name=obj.getname(obj.u2,user.getusername());
        int count=0;
        count=helper.getmsg_count(user.getusername(),table_name);
        if(count>1)
            return (count-1);
        else return count;
        //Toast.makeText(ctx," "+helper.getmsg_count(user.getusername(),table_name),Toast.LENGTH_SHORT).show();
       // return (helper.getmsg_count(user.getusername(),table_name));
         //return getMsg_count(obj.u2,obj.get_user2(),2);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }
    private String getnames(int pos ){
        ArrayList<String> al=new ArrayList<String>();
        al=helper.getunames();
        String a=al.get(pos);
        return a;
    }


   private String getmsgs(String from, String to,int position){
       String database_name=obj.getname(from,to);
       Cursor c=helper.getmsgs(from, to, database_name);
       ArrayList<String> msgs=new ArrayList<String>();
       if(c.moveToFirst()){
           do{
               msgs.add(c.getString(0));

           }while(c.moveToNext());
       }
       c.close();
       int length=msgs.size();
       if(length !=0)
           msg_count=length;
       else msg_count=1;
       if(length!=0)
       return msgs.get((length-(position%length)-1));

       else return "No messages to show";
    }


/*private int get_sent_recieved(String from,String to_user,int pos){
    String table_name=obj.getname(from,to_user);
    Cursor c=helper.get_touser(to_user,table_name);
    ArrayList<Integer> sent_recieved=new ArrayList<Integer>();
    if(c.moveToFirst()){
        do{
            if(c.getString(0).equals(to_user))
                sent_recieved.add(1);
            else
                sent_recieved.add(0);
        }while(c.moveToNext());

    }
    c.close();
    int length=sent_recieved.size();
    if(length !=0)
return sent_recieved.get((length-(pos%length)-1));
    else return 1;
}*/
    public byte[] getimages(String from,String touser,int position) {
        String db_name = obj.getname(from, touser);
        Cursor c = helper.getfndimages(touser, db_name);
        byte[] bytes=new byte[0];
        int count=0;
       List<byte[]> images = new ArrayList<byte[]>();
        if (c.moveToFirst()) {
            do {
               // if(position!=count);
                byte[] nbytes=c.getBlob(0);
                if(!(nbytes.length==bytes.length))
                   images.add(c.getBlob(0));
               // count++;

            } while (c.moveToNext());

        }
        c.close();
        int length=images.size();
       // Toast.makeText(ctx,""+length,Toast.LENGTH_SHORT).show();
        if(length != 0)
        bytes=images.get((length-(position%length))-1);
        else if(length ==0)
        {
         try{
         Uri   selectedimageuri= Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + ctx.getResources().getResourcePackageName(R.drawable.default_image)
                    + '/' + ctx.getResources().getResourceTypeName(R.drawable.default_image) + '/' + ctx.getResources().getResourceEntryName(R.drawable.default_image));
            InputStream iStream=ctx.getContentResolver().openInputStream(selectedimageuri);
            bytes= Utils.getBytes(iStream);

         }catch (IOException ioe) {
             Log.e(TAG, "<saveImageInDB> Error : " + ioe.getLocalizedMessage());
             helper.close();

         }
        }
        return bytes;
        //return length;
    }
 /*   public int getsize(String from,String touser){
        String name=obj.getname(from, touser);
        Cursor c=helper.getfndimages(touser,name);
        int count=0;
        if(c.moveToFirst()){
            do{
                count++;
            }while(c.moveToNext());

        }
        return count;
    }*/

    @Override
    public Object instantiateItem(final ViewGroup container,final int position) {
        String from=obj.u2;
        String to=user.getusername();
        int count=helper.get_unread(user.getusername(), obj.getname(from, to));
        //String date=helper.get_date(obj.getname(from,to));
        //String date=get_date(from,to,position);
       String db_name=obj.getname(from, to);
        layoutInflater= (LayoutInflater)ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        final View item_View=layoutInflater.inflate(R.layout.swipe_pages, container, false);
        TextView date_time=(TextView)item_View.findViewById(R.id.date_time);
        int count_all = helper.getmsg_count(user.getusername(), db_name);
        int msg_no=(count_all-position-1);
        if(msg_no !=0){
       // String time=get_date(from,to,position);
       String time=helper.get_date(db_name, msg_no);
        String c=time.substring(3, 5);
       String msg_date=time.substring(0,10);
       int date=Integer.parseInt(c);
        Date d=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        String formattedDate = sdf.format(d);

        int current_date=Integer.parseInt(formattedDate.substring(3, 5));
       // int check=get_sent_recieved(from, to, position);

        String msg_time=time.substring(10,21);
            if((current_date-date)!=0)
                date_time.setText("-"+msg_date);
            else
                date_time.setText("-"+msg_time);

        }
        int check=helper.get_sent_recieved(to,db_name,msg_no);
        byte[] bytes=getimages(from, to, position);
       // String msg=getmsgs(from, to, position);
        String msg=helper.getmsg(from,to,db_name,msg_no);
        byte[] nbytes=new byte[0];
       Bitmap map = Bitmap.createScaledBitmap(Utils.getImage(bytes), 200, 200, true);
        if(!bytes.equals(nbytes))
           map=blur(map,3);

        ImageView imgview=  (ImageView) item_View.findViewById(R.id.fndpic);
        imgview.setImageBitmap(map);
        TextView textView=(TextView)item_View.findViewById(R.id.swipetext);
        TextView sent_recieved=(TextView)item_View.findViewById(R.id.sent_recieved);
        sent_recieved.setPaintFlags(sent_recieved.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if(!msg.equals("No messages to show")){
        if(check==1){
        sent_recieved.setText("Recieved message");
        sent_recieved.setBackgroundColor(Color.GREEN);}
        else if  (check==0){
            sent_recieved.setText("Sent message");
            sent_recieved.setBackgroundColor(Color.RED);}}
        textView.setMovementMethod(new ScrollingMovementMethod());
        if(msg.length() !=0)
        textView.setText(msg);
        else
        textView.setText("An image was sent");
        container.addView(item_View);
       // container.removeView(item_View);
    /*    imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.removeView(item_View);
                notifyDataSetChanged();
                Toast.makeText(ctx,"position:"+position,Toast.LENGTH_SHORT).show();
            }
        });
*/  //item_View.setTag(container);
        return item_View;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
        //container.removeView(View;
    }
@Override
public int getItemPosition(Object object) {
    return POSITION_NONE;
}


    public Bitmap blur(Bitmap sentBitmap, int radius) {

             // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }
}
