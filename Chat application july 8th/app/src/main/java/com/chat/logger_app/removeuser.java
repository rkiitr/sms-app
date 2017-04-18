package com.chat.logger_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class removeuser extends AppCompatActivity {
    EditText id;
    Button b;
    DatabaseHelper dbhelper=new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remove);
        id=(EditText)findViewById(R.id.editText);
        b=(Button)findViewById(R.id.rb);
        remove();

    }
    public void remove(){
        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbhelper.deletefndhip(id.getText().toString());
                    int d=dbhelper.deletedata(id.getText().toString());
                      if(d>0)
                        Toast.makeText(removeuser.this,"User Removed",Toast.LENGTH_SHORT).show();
                        else
                          Toast.makeText(removeuser.this,"Invalid user id",Toast.LENGTH_SHORT).show();
                    }
                }

        );

    }
}
