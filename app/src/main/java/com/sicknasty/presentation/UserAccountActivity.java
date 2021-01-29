package com.sicknasty.presentation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sicknasty.R;
import com.sicknasty.business.AccessPages;
import com.sicknasty.business.AccessUsers;
import com.sicknasty.objects.Exceptions.ChangeUsernameException;
import com.sicknasty.objects.Exceptions.UserNotFoundException;
import com.sicknasty.objects.User;
import com.sicknasty.persistence.exceptions.DBPageNameNotFoundException;
import com.sicknasty.persistence.exceptions.DBUsernameExistsException;
import com.sicknasty.persistence.exceptions.DBUsernameNotFoundException;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UserAccountActivity extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    AccessUsers users=new AccessUsers();
    AccessPages pages=new AccessPages();
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        Button submitPass=findViewById(R.id.submitPassword);

        final EditText newPass=findViewById(R.id.newPasswordText);
        Button logout=findViewById(R.id.logout);
        submitPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences=getSharedPreferences("MY_PREFS",MODE_PRIVATE);
                String pass=newPass.getText().toString();
                if(pass.isEmpty()){
                    Toast.makeText(UserAccountActivity.this, "Enter a new Password", Toast.LENGTH_SHORT).show();
                }
                else{
                    //call similar functions
                    if(pass.length()<6)         //i did this bcz i didn't know how fn behaves
                        Toast.makeText(UserAccountActivity.this, "Enter a Password minimum 6 characters", Toast.LENGTH_SHORT).show();
                    else{

                        try {
                            users.updateUserPassword(preferences.getString("username",null),pass);
                            Toast.makeText(UserAccountActivity.this, "Password successfully updated", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(UserAccountActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                preferences=getSharedPreferences("MY_PREFS",MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.remove("username");
                editor.remove("password");
                editor.remove("isLogin");
                editor.apply();

                Intent newIntent=new Intent(UserAccountActivity.this,LoginActivity.class);
                startActivity(newIntent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        goToHome();
    }

    public void goToHome(){
        Intent intent=new Intent(UserAccountActivity.this,PageActivity.class);
        preferences=getSharedPreferences("MY_PREFS",MODE_PRIVATE);
        intent.putExtra("user",preferences.getString("username",null));
        startActivity(intent);
        finish();
    }
}