package com.sicknasty.objects;

import java.util.ArrayList;

public class CommunityPage extends Page {

    private ArrayList<User> bannedUsers;
    private String comName;

    public CommunityPage(String name, User creator) {
        super(creator);
        comName = name;
        bannedUsers = new ArrayList<>();
    }

    public String getComName(){         //gets communities name
        return comName;
    }

    public void banUser(User user) {     //deletes user from page and adds them to banned array list so they can follow again
        if(!bannedUsers.contains(user)){
            bannedUsers.add(user);
            getFollowers().remove(user);             //also remove from user's list
        }
    }
}
