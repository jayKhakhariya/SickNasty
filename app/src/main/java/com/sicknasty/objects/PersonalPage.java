package com.sicknasty.objects;

import java.util.ArrayList;

public class PersonalPage extends Page {                //for now!!!!
    public PersonalPage(User creator){
        super(creator);
    }

    @Override
    public ArrayList<Post> getPostList() {
        return super.getPostList();
    }
}