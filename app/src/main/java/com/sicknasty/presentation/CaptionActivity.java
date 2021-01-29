package com.sicknasty.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sicknasty.R;
import com.sicknasty.business.AccessPages;
import com.sicknasty.business.AccessPosts;
import com.sicknasty.business.AccessUsers;
import com.sicknasty.objects.Exceptions.NoValidPageException;
import com.sicknasty.objects.Exceptions.UserNotFoundException;
import com.sicknasty.objects.Page;
import com.sicknasty.objects.Post;
import com.sicknasty.objects.User;
import com.sicknasty.persistence.exceptions.DBPageNameNotFoundException;
import com.sicknasty.persistence.exceptions.DBPostIDExistsException;
import com.sicknasty.persistence.exceptions.DBUsernameNotFoundException;

public class CaptionActivity extends AppCompatActivity {

    AccessPosts posts=new AccessPosts();
    AccessUsers users=new AccessUsers();

    String updated=null;
    User curUser=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caption);

        final EditText caption=findViewById(R.id.captionText);
        Button postButton=findViewById(R.id.captionPost);
        ImageView imageView=findViewById(R.id.postImage);

        Intent intent=getIntent();


        final String uri=intent.getStringExtra("URI");
        Uri uri1 = Uri.parse(uri);
        imageView.setImageURI(uri1);


        try {
            curUser = users.getUser(intent.getStringExtra("pageName"));
        } catch (UserNotFoundException | DBUsernameNotFoundException e) {
            String errorMsg = e.getMessage();
            Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
        }

        final User finalCurUser = curUser;
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String captionText=caption.getText().toString();
                if(validateInput(captionText)){
                    updated="something";
                    try {
                        Post newPost=new Post(captionText, finalCurUser,uri,0,0,finalCurUser.getPersonalPage());
                        posts.insertPost(newPost);          //only insert after adding a caption(move to captionActivity)
                    } catch (DBPostIDExistsException e) {
                        // if this gets tripped, you have done something wrong
                        // -Lucas
                        Toast.makeText(CaptionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (NoValidPageException e) {
                        Toast.makeText(CaptionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Intent intent=new Intent(CaptionActivity.this,PageActivity.class);
                    intent.putExtra("user",finalCurUser.getUsername());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private boolean validateInput(String caption){
        String infoText = "";
        boolean result=true;


        //empty text??
        if(caption.length()>255){
            infoText = "caption is too long : must be less than 255 characters";
            result = false;
        }

        if(infoText.length() > 0) {
            Toast toast = Toast.makeText(CaptionActivity.this, infoText, Toast.LENGTH_SHORT);
            toast.show();
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        goToHome();
    }

    private void goToHome(){
        if(updated==null && curUser!=null){
            Intent intent=new Intent(CaptionActivity.this,PageActivity.class);
            intent.putExtra("user",curUser.getUsername());
            startActivity(intent);
            finish();
        }
    }
}
