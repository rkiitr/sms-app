package com.chat.logger_app;

import android.content.ContentResolver;
import android.content.Intent;
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


public class Signup extends AppCompatActivity{
     Button b;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
       b=(Button)findViewById(R.id.bsignup);
        logged=new user_logged_in(this);
        btnSelectImage = (ImageButton) findViewById(R.id.addimage);
        imgView = (AppCompatImageView) findViewById(R.id.imgView);
        btnSelectImage.setImageResource(R.drawable.add_userone);
        name=(EditText)findViewById(R.id.TFname);
        email=(EditText)findViewById(R.id.TFemail);
        uname=(EditText)findViewById(R.id.TFuname);
        pass=(EditText)findViewById(R.id.TFpass);
        cpass=(EditText)findViewById(R.id.TFconfirm);

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
               else if(name.getText().toString().length()>2)
                   flag1=1;


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
       email.setFocusable(false);

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
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
                    }
                    else if( !dbHelper.checkemail(email.getText().toString())){
                        email.setError("Email id already Used");
                        flag2=0;}

                    else if(email.getText().toString().length()>0)
                        flag2=1;

                }


            }
        });
        email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                email.setFocusableInTouchMode(true);

                return false;
            }
        });


        uname.setFocusable(false);

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
                   uname.setError("Username already taken");
                   flag3=0;}
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


        pass.setFocusable(false);
        pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                pass.setFocusableInTouchMode(true);

                return false;
            }
        });

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
                if(pass.getText().toString()==null)
                    flag4=0;
               else if (pass.getText().toString().length() <5 && pass.getText().toString().length()>0) {
                    if(pass.getText().toString() !=null){
                    pass.setError("password must be atleast 5 Char");
                    flag4 = 0;
                }}
                else if(pass.getText().toString().length()>0)
                  flag4 = 1;
            }
        });

        onClick();
        onSelect();

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
                    Bitmap map=Bitmap.createScaledBitmap(Utils.getImage(inputData), 240, 190, true);
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
   ;
    public void onClick(){

        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    String namestr=name.getText().toString();
                        String unamestr=uname.getText().toString();
                        String passstr=pass.getText().toString();
                        String cpassstr=cpass.getText().toString();
                        String emailstr=email.getText().toString();
                        boolean is_there=dbHelper.checkuname(unamestr);
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
                                byte[] inputData =new byte[0];
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
                                    selectedimageuri=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
       "://" + getResources().getResourcePackageName(R.drawable.no_image)
  + '/' + getResources().getResourceTypeName(R.drawable.no_image) + '/' + getResources().getResourceEntryName(R.drawable.no_image) );
                                    InputStream iStream=getContentResolver().openInputStream(selectedimageuri);
                                    inputData=Utils.getBytes(iStream);
                                }



                                if((flag1 * flag2 * flag3 * flag4) ==1){
                                 if(namestr.length()==0)
                                     Toast.makeText(Signup.this, "Please enter details correctly", Toast.LENGTH_SHORT).show();
                                 else if(emailstr.length()==0)
                                     Toast.makeText(Signup.this, "Please enter details correctly", Toast.LENGTH_SHORT).show();
                                 else if(unamestr.length()==0)
                                     Toast.makeText(Signup.this, "Please enter details correctly", Toast.LENGTH_SHORT).show();
                                 else if(passstr.length()==0)
                                     Toast.makeText(Signup.this, "Please enter details correctly", Toast.LENGTH_SHORT).show();
                                 else if(!dbHelper.checkemail(emailstr)){
                                     Toast.makeText(Signup.this,"Email id already used",Toast.LENGTH_SHORT).show();
                                // email.setFocusable(true);

                                 }
                                else  if(!dbHelper.checkuname(unamestr))
                                     Toast.makeText(Signup.this, "User already in database", Toast.LENGTH_SHORT).show();
                                     //Toast.makeText.(Signup.this, "Username Already there in Database", Toast.LENGTH_SHORT).show();
                                 else{
                                     dbHelper.insertContact(c, inputData);
                                 Toast.makeText(Signup.this, "User Entered into database", Toast.LENGTH_SHORT).show();
                                 name.setText("");
                                 email.setText("");
                                 uname.setText("");
                                 pass.setText("");
                                 cpass.setText("");
                                     Intent i=new Intent(Signup.this,display.class);
                                     i.putExtra("username",unamestr);
                                     i.putExtra("flag","from");
                                     logged.setUserLoggedIn(true);
                                     logged.store_user_data(unamestr);
                                     logged.store_useremail(emailstr);
                                   //  user_logged_in user
                                     startActivity(i);


                                 }
                             }

                            else
                                 Toast.makeText(Signup.this, "Please fill details correctly", Toast.LENGTH_SHORT).show();}
                            catch (IOException ioe) {
                                Log.e(TAG, "<saveImageInDB> Error : " + ioe.getLocalizedMessage());
                                dbHelper.close();

                            }
                        }
                        else
                            Toast.makeText(Signup.this, "Passwords don't match", Toast.LENGTH_SHORT).show();

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


}
