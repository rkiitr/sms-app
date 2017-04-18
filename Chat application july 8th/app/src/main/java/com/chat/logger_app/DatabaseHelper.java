package com.chat.logger_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
private static final int DATABASE_VESRSION=1;
    private static final String DATABASE_NAME="contacts.db";
    private static final String TABLE_NAME="contacts";
    private static final String TABLE2="friends";
    private static final String COLUMN_ID="id";
    private static final String COLUMN_NAME="name";
    private static final String COLUMN_EMAIL="Email";
    private static final String COLUMN_UNAME="uname";
    private static final String COLUMN_PASS="pass";
    public static final String IMAGE = "image";
    SQLiteDatabase db;
    private static final String Create_table="create table contacts (id INTEGER primary key AUTOINCREMENT  , "+
            "name text not null , email text not null , uname text not null , pass text not null , "+ IMAGE + " BLOB );";
   private static String createfndship="create table "+TABLE2+" (id INTEGER primary key AUTOINCREMENT  , "+
            "user1 text not null , user2 text not null , tablename text not null );";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VESRSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
     db.execSQL(Create_table);
     db.execSQL(createfndship);
        this.db=db;
    }


    public void createfndship(String uname1,String uname2){
      //  db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("user1",uname1);
        values.put("user2", uname2);
        values.put("tablename",uname1.concat(uname2));
        //values.put(COLUMN_PASS, c.getPass());
        //values.put(IMAGE, imageBytes);
        db.insert(TABLE2, null, values);
        db.close();
    }
public void create_fndship_table(String uname1, String uname2){
    db=this.getWritableDatabase();
    String table_name=uname1.concat(uname2);
    String query="create table "+table_name+" (id INTEGER primary key AUTOINCREMENT , msgfrom text not null , msgto text not null , msg text , image BLOB , read INTEGER , date_time text );";
    db.execSQL(query);
}
    public boolean delete_chat(String table_name){
        db=this.getWritableDatabase();
        boolean deleted=false;
        String query="delete  from "+table_name;
        //db.rawQuery(query,null);
      int count=  db.delete(table_name, null, null);
        if(count !=0)
            deleted=true;
        else deleted=false;
        return deleted;
    }
public void sendmsg(String fromuser, String touser,String dbname,String msg,byte[] imageBytes,String time){
    db=this.getWritableDatabase();
    ContentValues cv=new ContentValues();
    cv.put("msgfrom",fromuser);
    cv.put("msgto",touser);
    cv.put("msg",msg);
    cv.put("image",imageBytes);
    cv.put("read",1);
    cv.put("date_time",time);
    //cv.put("at",System.currentTimeMillis());
    db.insert(dbname,null,cv);
    db.close();
}
public String getmsg(String from,String to,String db_name,int position){
    Cursor c=getmsgs(from, to, db_name);
    String msg="";
    int count=1;
    if(position==0)
        msg="No messages to show";
        //return "No messages to show";
    else
    {
        if(c.moveToFirst()){
            do{
                if(count==position)
                {
                    msg=c.getString(0);
                    break;

                }
                count++;

            }while(c.moveToNext());

        }

    }
  return msg;
}
   public String get_date(String table_name,int position){
        db=this.getReadableDatabase();
        String query="select date_time from "+table_name;
        Cursor c=db.rawQuery(query,null);
       int count=1;
       String date="";
       if(position==0)
           date="";
       else if(c.moveToFirst()){
           do{
               if(count==position){
               date=c.getString(0);
               break;
               }
               count++;
           }while(c.moveToNext());

       }
    // return c;
       return date;
    }
    public int get_sent_recieved(String to_user,String table_name,int pos){
        Cursor c=get_touser(to_user,table_name);
        int count=1;
        int sent_recieved=0;
        if(pos==0)
            sent_recieved=1;
        else
            if(c.moveToFirst()){
                do{
                    if(count==pos){
                        if(c.getString(0).equals(to_user))
                            sent_recieved=1;
                        else
                            sent_recieved=0;


                    }
                    count++;
                }while (c.moveToNext());

            }
        return sent_recieved;
    }
    public boolean delete_msg(int position,String table_name){
        db=this.getWritableDatabase();
        String query="select * from "+table_name;
        int count=1;
        Boolean deleted=false;
        String id="";
        Cursor c=db.rawQuery(query,null);
        if(position==0){
            deleted=false;
            return deleted;
        }
        else
        {
            if(c.moveToFirst()){
                do{
                    if(count==position){
                        id=c.getString(0);
                        break;
                    }
                    count++;
                }while(c.moveToNext());

            }
            //  String id=c.getString(0);
            int rows_deleted=db.delete(table_name,"id=? ", new String[]{id});
            if(rows_deleted>0)
                deleted=true;

        }
        c.close();
        return deleted;
    }
    public Cursor getfndimages(String touser,String table_name){
        db=this.getReadableDatabase();
        String query="select image from "+table_name;
        Cursor c=db.rawQuery(query,null);
        return c;

    }
    public Cursor getmsgs(String fromuser ,String touser,String table_name){
        db=this.getReadableDatabase();
        //String query="select msg from "+table_name+ " where msgto= '"+touser+"'";
        String query="select msg from "+table_name;
        Cursor c=db.rawQuery(query,null);
        return c;
    }
   public Cursor get_touser(String logged_user,String table_name){
       db=this.getReadableDatabase();
       String query="select msgto from "+table_name;
       Cursor c=db.rawQuery(query,null);
       return c;
   }
    public int getmsg_count(String touser,String table_name){
        db=this.getReadableDatabase();
        String query="select * from "+table_name;
        Cursor c=db.rawQuery(query,null);
        int count=1;
        if(c.moveToFirst())
            do{
                //if(c.getString(2).equals(touser))
                    count=count+1;
            }while(c.moveToNext());
        return count;
    }

//public void delete_chat

    public void update_unread(String from_user,String table_name){
        db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        Integer read_new=0;
        cv.put("read",read_new);
        long result=db.update(table_name, cv, "msgfrom= ?",new String[]{from_user});

    }


public int get_unread(String touser,String table_name){
    db=this.getReadableDatabase();
    String query="select read from "+table_name+ " where msgto= '"+touser+"'";
    Cursor c=db.rawQuery(query,null);
    int count=0;
    if(c.moveToFirst()){
        do{
            if(c.getInt(0)== 1)
                count++;
        }while(c.moveToNext());

    }
    c.close();
    return count;

}
    public String getdatabase_name(String user1, String user2){
        db=this.getReadableDatabase();
        String query="select * from "+TABLE2;
        String table_name="";
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                if(  (c.getString(1).equals(user1) && c.getString(2).equals(user2) ) || (c.getString(1).equals(user2) && c.getString(2).equals(user1)) )
                    table_name= c.getString(3);
            }while(c.moveToNext());
        }

        c.close();
        return table_name;
    }
public void deletefndhip(String id){
    db=this.getReadableDatabase();
    String query="SELECT uname FROM "+TABLE_NAME+ " WHERE id = '"+id+"'";
    Cursor c=db.rawQuery(query,null);
    String uname;
    if(c.moveToFirst()){
      uname=c.getString(0);
    }else
        uname="";
    if(uname !=null){
        String query2="select * from "+TABLE2;
        Cursor c2=db.rawQuery(query2,null);
        if(c2.moveToFirst()){

            do{
                if(c2.getString(1).equals(uname) || c2.getString(2).equals(uname)){
                    String col=c2.getString(0);
                  db.delete(TABLE2, "id=? ",new String[]{col});
                }

            }while(c2.moveToNext());


        }


    }
}

    public Integer deletedata(String id)
    {
        Integer count;
        SQLiteDatabase db=this.getWritableDatabase();

        //  db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE NAME = '"+Table_Name+"'");
        count= db.delete(TABLE_NAME,"ID=? ", new String[]{id});
        //db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE NAME = '" + TABLE_NAME + "'");
        return count;
    }
    public void update_uname_fndship(String oldname,String updated_name){
     //ContentValues cv=new ContentValues();
        String query="select * from "+TABLE2;
        Cursor c=db.rawQuery(query, null);
       // String friend2="";
        String a="";
        String b="";
        String col_id;
        if(c.moveToFirst()){
         do{
             a=c.getString(1);
             b=c.getString(2);
             if(a.equals(oldname)){
                 ContentValues cv=new ContentValues();
                 cv.put("user1",updated_name);
                 col_id=c.getString(0);
                 db.update(TABLE2,cv,"ID=? ", new String[]{col_id});
             }
             if(b.equals(oldname)){
                 ContentValues cv=new ContentValues();
                 cv.put("user2",updated_name);
                 col_id=c.getString(0);
                 db.update(TABLE2,cv,"ID=? ", new String[]{col_id});
             }

         }while(c.moveToNext());

        }
    }



    public boolean update_user_profile(String Name, String email, String username, String password, byte[] image,String original_uname){
        db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("name",Name);
        cv.put("email",email);
        cv.put("uname",username);
        cv.put("pass",password);
        cv.put("image", image);
        long result=db.update(TABLE_NAME, cv, "uname= ?",new String[]{original_uname});
        if(result==-1)
            return false;
        else
        return true;
    }


public void insertContact(Contact c,byte[] imageBytes){
db=this.getWritableDatabase();
    ContentValues values=new ContentValues();
    String query="select * from contacts ";
    Cursor cursor=db.rawQuery(query,null);
    int count=cursor.getCount();
   // values.put(COLUMN_ID,count);
    values.put(COLUMN_NAME,c.getName());
    values.put(COLUMN_EMAIL, c.getemail());
    values.put(COLUMN_UNAME,c.getUname());
    values.put(COLUMN_PASS, c.getPass());
    values.put(IMAGE, imageBytes);
    db.insert(TABLE_NAME, null, values);
    db.close();
}

    public int getitemcount(){
        db=this.getReadableDatabase();
        String query="select * from contacts ";
        int count=0;
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                count++;

            }while(c.moveToNext());

        }
        c.close();
        return count;
    }
   public void delete_fndship_table(String table_name){
       db=this.getWritableDatabase();
      // String delete="Drop TABLE IF EXISTS "+table_name;
       db.execSQL("DROP TABLE IF EXISTS " + table_name );
       //db.delete(table_name,null,null);

          }

    public boolean getfndhip(String u1,String u2){
        db=this.getReadableDatabase();
        String query2="select * from friends ";
        Cursor c2=db.rawQuery(query2,null);
        String a="";
        String b="";
        boolean fnd=false;
        if(c2.moveToFirst()){
            do{
                a=c2.getString(1);
                b=c2.getString(2);
                if( a.equals(u1) && b.equals(u2))
                    fnd=true;
                else if(a.equals(u2) && b.equals(u1))
                    fnd=true;

            }while (c2.moveToNext());


        }
        else
            fnd=false;
        c2.close();
        return fnd;
    }

    public ArrayList<String> getnotfriends(String username){
        db=this.getReadableDatabase();
        ArrayList<String> unames=new ArrayList<String>();
        String query1="select * from contacts ";
        String query2="select * from friends ";
        Cursor c1=db.rawQuery(query1,null);
        Cursor c2=db.rawQuery(query2,null);
        String uname;
        if(c1.moveToFirst())
        {
            do{
                uname=c1.getString(3);
                if(!uname.equals(username))
                {
                    if(!getfndhip(username,uname))
                       unames.add(uname);
                }

            }while(c1.moveToNext());

        }

        return unames;
    }

    public ArrayList<String> getFriends(String username){
        db=this.getReadableDatabase();
        ArrayList<String> unames=new ArrayList<String>();
        String query1="select * from contacts ";
        Cursor c=db.rawQuery(query1,null);
        String uname;
        if(c.moveToFirst()){
            do{
                uname=c.getString(3);
                if(!uname.equals(username)){
                    if(getfndhip(username,uname))
                        unames.add(uname);
                }

            }while(c.moveToNext());
        }
        c.close();
        return unames;
    }
    public ArrayList<String> getunames(){
        db=this.getReadableDatabase();
        ArrayList<String> unames=new ArrayList<String>();
        String query="select * from contacts ";
        Cursor c=db.rawQuery(query,null);
        String a=null;
        if(c.moveToFirst()){
            do{
                a=c.getString(3);
                unames.add(a);

            }while(c.moveToNext());

        }
        return unames;

    }
    public ArrayList<String> getFname(String name){

        db=this.getReadableDatabase();
        ArrayList<String> names=new ArrayList<String>();
        String query ="select * from contacts ";
        Cursor c=db.rawQuery(query,null);
        String a;
        if(c.moveToFirst()){
            do{a=c.getString(3);
                if(a.equals(name))
                    names.add(c.getString(1));
            }while(c.moveToNext());

        }
        return names;
    }

    public Cursor getuserData(String user){
        SQLiteDatabase db=this.getReadableDatabase();
        //error in query
        String query= "SELECT * FROM "+TABLE_NAME+ " WHERE uname = '"+user+"'";
        // String query="select * from "+TABLE_NAME + " where uname "+user;
        Cursor u_data=db.rawQuery(query, null);
        return u_data;
    }

    public boolean checkuname(String username){
        db=this.getReadableDatabase();
        String query="select uname from "+TABLE_NAME;
        Cursor c=db.rawQuery(query, null);
        String a;
        boolean is_there=true;
        if(c.moveToFirst()){
    do{
        a=c.getString(0);
        if(a.equals(username))
           is_there=false;
       // break;
    }while(c.moveToNext());
        }

           return is_there;

    }
    public boolean checkemail(String email){
        db=this.getReadableDatabase();
        String query="select Email from "+TABLE_NAME;
        Cursor c=db.rawQuery(query, null);
        String a;
        boolean is_there=true;
        if(c.moveToFirst()){
            do{
                a=c.getString(0);
                if(a.equals(email))
                    is_there=false;
                // break;
            }while(c.moveToNext());
        }

        return is_there;

    }

    public String getfndhipcol(String u1,String u2){
        db=this.getReadableDatabase();
        String query2="select * from friends ";
        Cursor c2=db.rawQuery(query2,null);
        String col_id="";
        String a="";
        String b="";
        boolean fnd=false;
        if(c2.moveToFirst()){
            do{
                a=c2.getString(1);
                b=c2.getString(2);
                if( a.equals(u1) && b.equals(u2))
                    col_id=(c2.getString(0));
                else if(a.equals(u2) && b.equals(u1))
                    col_id=(c2.getString(0));

            }while (c2.moveToNext());


        }
        else
            col_id="";
       return col_id;
    }


    public void deletefndship(String uname,String uname2){
   String col=getfndhipcol(uname,uname2);
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE2,"ID=? ", new String[]{col});

    }

    public String searchpass(String uname){
        db=this.getReadableDatabase();
       String query="select uname, pass,image from "+TABLE_NAME;
        Cursor cursor=db.rawQuery(query, null);
     String a,b;
        b="not found";
        if(cursor.moveToFirst()){

            do{
a=cursor.getString(0);
                if(a.equals(uname)){
                    b=cursor.getString(1);
                break;
                }
            }while(cursor.moveToNext());
        }
        return b;
    }
    public String getemail(String uname){
        db=this.getReadableDatabase();
        String query="select uname, Email from "+TABLE_NAME;
        Cursor c=db.rawQuery(query,null);
        String a,b;
        b="not found";
        if(c.moveToFirst()){

            do{
                a=c.getString(0);
                if(a.equals(uname))
                {
                    b=c.getString(1);
                    break;
                }
            }while(c.moveToNext());
        }
        return b;
    }
    public String getfullname(String uname){

        db=this.getReadableDatabase();
        String query="select uname, name from "+TABLE_NAME;
        Cursor c=db.rawQuery(query,null);
        String a,b;
        b="not found";
        if(c.moveToFirst())
        {
            do{
                a=c.getString(0);
                if(a.equals(uname)){
                    b=c.getString(1);
                   break;

                }

            }while(c.moveToNext());

        }
        return b;
    }

    public Cursor getAllData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+TABLE_NAME, null);
        return res;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
String query ="Drop TABLE IF EXISTS "+TABLE_NAME;
        String query2="Drop TABLE IF EXISTS "+TABLE2;
        db.execSQL(query);
        db.execSQL(query2);
        this.onCreate(db);

    }


    public byte[] getImage(String uname){
        db=this.getReadableDatabase();
        String query="select uname, pass,image from "+TABLE_NAME;
        Cursor cursor=db.rawQuery(query, null);
        String a,b;
        b="not found";
        byte[] blob = new byte[0];
        if(cursor.moveToFirst()){

            do{
                a=cursor.getString(0);
                if(a.equals(uname)){
                    b=cursor.getString(1);
                    blob = cursor.getBlob(cursor.getColumnIndex(IMAGE));
                    break;
                }
            }while(cursor.moveToNext());
        }
        return blob;
    }

}
