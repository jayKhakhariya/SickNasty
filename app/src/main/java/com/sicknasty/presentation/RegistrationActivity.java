package com.sicknasty.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sicknasty.business.AccessPages;
import com.sicknasty.business.AccessUsers;
import com.sicknasty.objects.User;

import com.sicknasty.R;

public class RegistrationActivity extends AppCompatActivity {

    AccessUsers users=new AccessUsers();
    AccessPages pages = new AccessPages();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Button signIn=findViewById(R.id.signIn);
        Button register=findViewById(R.id.Register);
        //validate the new account and create it
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get the users information
                String name = ((EditText)findViewById(R.id.signUpName)).getText().toString();
                String username = ((EditText)findViewById(R.id.signUpUsername)).getText().toString();
                String password = ((EditText)findViewById(R.id.signUpPassword)).getText().toString();

                //some text to show the user errors, or if we were successful
                String infoText = "An unexpected error has occurred. Please try again."; User newUser = null;

                //we try to create a user with the input given. If there is an error it is handled
                //in the catch block
                try {
                    newUser = new User(name, username, password);
                    infoText = "Sign Up Successful";
                    users.insertUser(newUser);          //if the user was created without error, insert to db
                    pages.insertNewPage(newUser.getPersonalPage());
                    onBackPressed(); //I don't know what this does. Please comment
                } catch(Exception e) {
                    infoText = e.getMessage(); //get the error message and display it
                } finally {
                    //display some text to the user
                    Toast toast = Toast.makeText(getApplicationContext(),infoText ,Toast.LENGTH_SHORT);
                    toast.show();
                }//finally
                }//onClickView
        });

        //here we listen for a click, and attempt to register
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(startIntent);
            }
        });
    }

}
