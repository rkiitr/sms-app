package com.chat.logger_app;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Edit_prof_form extends AppCompatActivity {

    Button b;
    Button rimage;
    Button cancel;
    int flag1=0;
    int flag2=0;
    int flag3=0;
    int flag4=0;

    user_logged_in logged;
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "MainActivity";

    ImageButton btnSelectImage;
    // boolean space;
    AppCompatImageView imgView;
    Uri selectedimageuri;
    EditText name,email,uname, pass, cpass;
    DatabaseHelper dbHelper=new DatabaseHelper(this);
    user_logged_in data;
    String current_uname;
    String full_name;
    String current_email;
    String password;
    String spassword;
      byte[] inputData =new byte[0];
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_prof_page);
        b=(Button)findViewById(R.id.bsignup);
        cancel=(Button)findViewById(R.id.move_back);
        logged=new user_logged_in(this);
        data=new user_logged_in(this);
        rimage=(Button)findViewById(R.id.R_image);
        btnSelectImage = (ImageButton) findViewById(R.id.addimage);
        imgView = (AppCompatImageView) findViewById(R.id.imgView);
        btnSelectImage.setImageResource(R.drawable.add_userone);
        name=(EditText)findViewById(R.id.TFname);
        email=(EditText)findViewById(R.id.TFemail);
        uname=(EditText)findViewById(R.id.TFuname);
        pass=(EditText)findViewById(R.id.TFpass);
        cpass=(EditText)findViewById(R.id.TFconfirm);
        current_email=data.getemail();
        current_uname=data.getusername();
        full_name="Custom name";
        Cursor data=dbHelper.getAllData();
        while(data.moveToNext()){
            String uname=data.getString(3);
            if(uname.equals(current_uname)){
                full_name=data.getString(1);
                break;}
        }
        name.setText(full_name);
        email.setText(current_email);
        uname.setText(current_uname);
      // Toast.makeText(Edit_prof_form.this,"Current uname: "+current_uname,Toast.LENGTH_SHORT).show();
        byte[] bytes = dbHelper.getImage(current_uname);

        btnSelectImage.setImageBitmap(Utils.getImage(bytes));


        btnSelectImage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int a = 0;
            }
        });

        InputFilter filter = new InputFilter() {
            boolean canEnterSpace = false;

            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {



                if(name.getText().toString().equals(""))
                {
                    canEnterSpace = false;
                }

                StringBuilder builder = new StringBuilder();
                if(name.getText().toString().length() <20){
                    for (int i = start; i < end; i++) {
                        char currentChar = source.charAt(i);

                        if (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
                            builder.append(currentChar);
                            canEnterSpace = true;
                        }

                        if(Character.isWhitespace(currentChar) && canEnterSpace) {
                            builder.append(currentChar);
                        }


                    }
                    return builder.toString();
                }

                else return builder.toString();
            }

        };


        name.setFilters(new InputFilter[]{filter});
        // email.setFilters(new InputFilter[]{filter});

        name.setFocusable(false);
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (name.getText().toString() == null) {
                    flag1 = 0;
                } else if (name.getText().toString().length() > 0 && name.getText().toString().length() < 3) {
                    //name.setError(null);
                    if (name.getText().toString() != null) {
                        name.setError("Name must be atleast 3 Char");
                        flag1 = 0;

                    }
                } //else if (name.getText().toString() !=null) flag1 = 0;
                else if (name.getText().toString().length() > 2)
                    flag1 = 1;


            }
        });
       /* name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                name.setError(null);
            }
        });*/

        name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                name.setFocusableInTouchMode(true);

                return false;
            }
        });

//final String current_email=data.getemail();
       // if(email.getText().toString() != current_email){
            email.setFocusable(false);


        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
             //   String current_email = data.getemail();
               // if (email.getText().toString() != current_email){
                    if (email.getText().toString() == null)
                        flag2 = 0;
                    else if (email.getText().toString().length() < 5 && email.getText().toString().length() > 0) {
                        if (email.getText().toString() != null) {
                            email.setError("Email must be atleast 5 char");
                            flag2 = 0;
                        }

                    }
                    //else if (email.getText().toString() == null) flag2 = 0;
                    else if (email.getText().toString().length() > 0) {
                        if (!validemail(email.getText().toString())) {
                            email.setError("Email pattern not valid and shouldn't have spaces");
                            flag2 = 0;
                        } else if (!dbHelper.checkemail(email.getText().toString())) {
                            boolean match=(current_email.equals(email.getText().toString()));
                            if(match)
                                flag2=1;
                            else{
                            email.setError("Email id already Used");
                            flag2 = 0;}
                        } else if (email.getText().toString().length() > 0)
                            flag2 = 1;

                    }
           // }
             //   else return;

            }
        });

            email.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    email.setFocusableInTouchMode(true);

                    return false;
                }
            });
      //  }

     /*   email.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                email.setError(null);
            }
        });*/

        uname.setFocusable(false);
       // final String current_uname=data.getusername();

        uname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (uname.getText().toString() == null){
                    flag3 = 0;}
                else if (uname.getText().toString().length() < 5 && uname.getText().toString().contains("") && uname.getText().toString().length() > 0) {
                    if (uname.getText().toString() != null) {
                        uname.setError("Uname must be atleast 5 Char and withouspaces");
                        flag3 = 0;
                    }
                }

                else if (uname.getText().toString().contains(" ")) {
                    uname.setError("No Spaces Allowed");


                    // Toast.makeText(MyActivity.this, "No Spaces Allowed", 5000).show();
                }
                else if(uname.getText().toString().length()>4 && !dbHelper.checkuname(uname.getText().toString())){
                    if(uname.getText().toString().equals(current_uname))
                        flag3=1;
                    else{

                    uname.setError("Username already taken");
                    flag3=0;}

                }
                else if (uname.getText().toString().length() > 4) flag3 = 1;
                // else flag3=1;*/
            }
        });

        uname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                uname.setFocusableInTouchMode(true);

                return false;
            }
        });
        /*uname.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                uname.setError(null);
            }
        });*/

        pass.setFocusable(false);
        pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                pass.setFocusableInTouchMode(true);

                return false;
            }
        });
      /*  pass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pass.setError(null);
            }
        });*/
        cpass.setFocusable(false);
        cpass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                cpass.setFocusableInTouchMode(true);

                return false;
            }
        });
        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (pass.getText().toString() == null)
                    flag4 = 0;
                else if (pass.getText().toString().length() < 5 && pass.getText().toString().length() > 0) {
                    if (pass.getText().toString() != null) {
                        pass.setError("password must be atleast 5 Char");
                        flag4 = 0;
                    }
                } else if (pass.getText().toString().length() > 0)
                    flag4 = 1;
            }
        });

        onClick();
        onSelect();
        cancel_changes();
        onRemoveImage();

    }
 void onRemoveImage(){
     rimage.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
  btnSelectImage.setImageResource(R.drawable.add_userone);
 selectedimageuri=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
"://" + getResources().getResourcePackageName(R.drawable.no_image)
   + '/' + getResources().getResourceTypeName(R.drawable.no_image) + '/' + getResources().getResourceEntryName(R.drawable.no_image) );

         }
     });

 }

    void onSelect(){
        btnSelectImage.setOnClickListener(
                new    View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        openImageChooser();

                    }
                }

        );

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                selectedimageuri = data.getData();
                try {
                    InputStream iStream=getContentResolver().openInputStream(selectedimageuri);
                    byte[] inputData = Utils.getBytes(iStream);
                    Bitmap map=Bitmap.createScaledBitmap(Utils.getImage(inputData), 240, 220, true);
                    btnSelectImage.setImageBitmap(map);
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

    public void cancel_changes(){
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

    }
    public void onClick(){

        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                final        String namestr=name.getText().toString();
                  final      String unamestr=uname.getText().toString();
                    final    String passstr=pass.getText().toString();
                     final   String cpassstr=cpass.getText().toString();
                      final  String emailstr=email.getText().toString();
                        boolean is_there=dbHelper.checkuname(unamestr);
                        // Toast.makeText(Signup.this," "+flag1 +" "+ flag2 + " "+flag3+ " "+flag4+ " "+ is_there+ " "+ namestr.length(),Toast.LENGTH_SHORT).show();
                        cpass.setFocusable(false);
                        uname.setFocusable(false);
                        email.setFocusable(false);
                        name.setFocusable(false);
                        pass.setFocusable(false);

                        if(passstr.equals(cpassstr)){

                            Contact c=new Contact();
                            c.setName(namestr);
                            c.setemail(emailstr);
                            c.setUname(unamestr);
                            c.setPass(passstr);
                            try{

                                if(selectedimageuri != null ){
                                    String size = Long.toString(new File(selectedimageuri.getPath()).length());
                                    // int foo = Integer.parseInt(size);
                                    InputStream iStream = getContentResolver().openInputStream(selectedimageuri);
                                    inputData=Utils.getBytes(iStream);
                                    Bitmap map=Bitmap.createScaledBitmap(Utils.getImage(inputData), 400, 300, true);
                                    inputData=Utils.getImageBytes(map);
                                }
                                else
                                {
                                    inputData=dbHelper.getImage(current_uname);

                                }
                              /*  else
                                {
                                    selectedimageuri=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                                            "://" + getResources().getResourcePackageName(R.drawable.no_image)
                                            + '/' + getResources().getResourceTypeName(R.drawable.no_image) + '/' + getResources().getResourceEntryName(R.drawable.no_image) );
                                    // selectedimageuri=Uri.parse("android.resource://package com.jiochat.logger_app/drawable/no_image");
                                    InputStream iStream=getContentResolver().openInputStream(selectedimageuri);
                                    inputData=Utils.getBytes(iStream);
                                }*/
                                if(namestr.equals(full_name))
                                    flag1=1;
                                if(emailstr.equals(current_email))
                                    flag2=1;
                                if(unamestr.equals(current_uname))
                                    flag3=1;



                                if((flag1 * flag2 * flag3 * flag4) ==1){
                                    if(namestr.length()==0)
                                        Toast.makeText(Edit_prof_form.this, "Please enter details correctly", Toast.LENGTH_SHORT).show();
                                    else if(emailstr.length()==0)
                                        Toast.makeText(Edit_prof_form.this, "Please enter details correctly", Toast.LENGTH_SHORT).show();
                                    else if(unamestr.length()==0)
                                        Toast.makeText(Edit_prof_form.this, "Please enter details correctly", Toast.LENGTH_SHORT).show();
                                    else if(passstr.length()==0)
                                        Toast.makeText(Edit_prof_form.this, "Please enter details correctly", Toast.LENGTH_SHORT).show();
                                    else if(!dbHelper.checkemail(emailstr) && !emailstr.equals(current_email)){
                                        Toast.makeText(Edit_prof_form.this,"Email id already used",Toast.LENGTH_SHORT).show();
                                        // email.setFocusable(true);

                                    }
                                    else  if(!dbHelper.checkuname(unamestr) && !unamestr.equals(current_uname))
                                        Toast.makeText(Edit_prof_form.this, "Username already taken", Toast.LENGTH_SHORT).show();
                                        //Toast.makeText.(Signup.this, "Username Already there in Database", Toast.LENGTH_SHORT).show();
                                    else{

                   final   View view=(LayoutInflater.from(Edit_prof_form.this)).inflate(R.layout.edit_prof_dialog,null);
                AlertDialog.Builder confirm=new AlertDialog.Builder(Edit_prof_form.this);
         confirm.setView(view);
         final EditText cpass=(EditText)view.findViewById(R.id.cpass);
         confirm.setTitle("Confirm details");
                                        confirm.setPositiveButton("Confirm",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //Do nothing here because we override this button later to change the close behaviour.
                                                        //However, we still need this because on older versions of Android unless we
                                                        //pass a handler the button doesn't get instantiated
                                                    }
                                                });
           final AlertDialog dialog = confirm.create();
            dialog.show();

                                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Boolean wantToCloseDialog = false;
                                                //cname = (EditText) view.findViewById(R.id.cusername);

                                                // String username=getusername();
                                                //Toast.makeText(getActivity(),"Email: "+name,Toast.LENGTH_SHORT).show();
                                                //  if (username.equals(cname.getText().toString())) {
                                                // String str = cname.getText().toString();
                                                boolean is_there = false;
                                                // Toast.makeText(getActivity(),"username: "+username,Toast.LENGTH_SHORT).show();
                                                // is_there = dbhelper.checkuname(username);
                                                password = cpass.getText().toString();
                                                spassword = dbHelper.searchpass(current_uname);
                                                if (password.length() == 0)
                                                    Toast.makeText(Edit_prof_form.this, "Enter the Password", Toast.LENGTH_SHORT).show();

                                                else if (password.equals(spassword)) {
                                                    wantToCloseDialog = true;
                                                    cpass.setText("");
                                                    dbHelper.update_user_profile(namestr, emailstr, unamestr, passstr, inputData, current_uname);
                                                    dbHelper.update_uname_fndship(current_uname, unamestr);
                                                    // dbHelper.insertContact(c, inputData);
                                                    Toast.makeText(Edit_prof_form.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                                    name.setText("");
                                                    email.setText("");
                                                    uname.setText("");
                                                    pass.setText("");
                                                    cpass.setText("");

                                                    logged.setUserLoggedIn(true);
                                                    logged.store_user_data(unamestr);
                                                    logged.store_useremail(emailstr);
                                                    Intent i=new Intent(Edit_prof_form.this,display.class);
                                                    i.putExtra("username",unamestr);
                                                    i.putExtra("flag","from");
                                                    //  user_logged_in user
                                                    startActivity(i);
                                                    finish();
                                                    //   user.setUserLoggedIn(true);
                                                } else
                                                    Toast.makeText(Edit_prof_form.this, "Wrong password", Toast.LENGTH_SHORT).show();

                                                // }
                                                // else
                                                //    Toast.makeText(getActivity(), "Wrong user name", Toast.LENGTH_SHORT).show();
                                                //Do stuff, possibly set wantToCloseDialog to true then...
                                                if (wantToCloseDialog)
                                                    dialog.dismiss();
                                                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                                            }
                                        });











                                    }
                                }

                                else
                                    Toast.makeText(Edit_prof_form.this, "Please fill details correctly ", Toast.LENGTH_SHORT).show();}
                            catch (IOException ioe) {
                                Log.e(TAG, "<saveImageInDB> Error : " + ioe.getLocalizedMessage());
                                dbHelper.close();

                            }
                        }
                        else
                            Toast.makeText(Edit_prof_form.this, "Passwords don't match", Toast.LENGTH_SHORT).show();

                    }
                }

        );
    }

    public boolean validemail(String email){
        if (email == null) {
            return false;
        } else {
            return   !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }

    }
    @Override
    public void onBackPressed(){

        finish();
    }



}
