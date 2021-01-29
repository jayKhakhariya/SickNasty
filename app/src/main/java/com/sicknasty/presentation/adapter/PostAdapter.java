package com.sicknasty.presentation.adapter;

import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.sicknasty.objects.*;
import com.sicknasty.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PostAdapter extends ArrayAdapter<Post> {
    private int resourceId;

    public PostAdapter(@NonNull Context context, int resource , List<Post> posts) {
        super(context,resource,posts);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        final ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(//convertView is null represent layout is not loaded, and it mean that getView is not called
                    resourceId, null);

            viewHolder.ivImage =  view.findViewById(R.id.ivImage);
            viewHolder.userName = view.findViewById(R.id.userName);
            viewHolder.textView = view.findViewById(R.id.textView);

            view.setTag(viewHolder);
        }else{
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }

        Post post =getItem(getCount()-position-1);           //give a post position in layout(now it displays the most recent one)


        //get the path from the post and display it
        //lucas check the following code(setImageUri accepts an URI)

        Uri postUri;
        if (post != null) {
            postUri = Uri.parse(post.getPath());
            viewHolder.ivImage.setImageURI(postUri);             //this is working
            viewHolder.userName.setText(post.getUserId().getUsername());
            viewHolder.textView.setText(post.getText());
        }

        return view;
    }
}
class ViewHolder{
    ImageView ivImage;
    TextView textView;
    TextView userName;
}

