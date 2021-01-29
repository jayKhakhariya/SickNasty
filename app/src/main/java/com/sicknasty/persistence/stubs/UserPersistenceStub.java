package com.sicknasty.persistence.stubs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sicknasty.objects.Exceptions.PasswordErrorException;
import com.sicknasty.objects.User;
import com.sicknasty.persistence.UserPersistence;
import com.sicknasty.persistence.exceptions.DBUsernameExistsException;
import com.sicknasty.persistence.exceptions.DBUsernameNotFoundException;

public class UserPersistenceStub implements UserPersistence {
    private HashMap<String, User> users;

    public UserPersistenceStub() {
        this.users = new HashMap<String, User>();
    }

    @Override
    public User getUser(String username) throws DBUsernameNotFoundException {
        if (username == null) return null; //this should never happen

        if (!this.users.containsKey(username)) throw new DBUsernameNotFoundException(username);

        // this will return the User object at that id
        return this.users.get(username);
    }

    @Override
    public ArrayList<String> getAllUsers() {
        ArrayList<String> usernames = new ArrayList<String>();

        for (String k : this.users.keySet()) {
            usernames.add(k);
        }

        return usernames;
    }

    @Override
    public User insertNewUser(User user) throws DBUsernameExistsException {
        if (user == null) return null;

        if (!users.containsKey(user.getUsername())) {
            this.users.put(user.getUsername(),user);

            // cool thing is, is that this will return null on failure and the object on success
            return users.get(user.getUsername());
        } else {
            throw new DBUsernameExistsException(user.getUsername());
        }
    }

    @Override
    public boolean deleteUser(User user) {
        if (user == null) return false;

        User result = this.users.remove(user.getUsername());

        return result != null;
    }

    @Override
    public boolean updateUsername(String oldUsername, String newUsername) throws DBUsernameExistsException, DBUsernameNotFoundException {
        if (this.users.containsKey(newUsername)) throw new DBUsernameExistsException(newUsername);

        if (this.users.containsKey(oldUsername)) {
            User oldUser = this.users.get(oldUsername);

            this.users.remove(oldUsername);
            this.users.put(newUsername, oldUser);

            return true;
        } else {
            throw new DBUsernameNotFoundException(oldUsername);
        }
    }

    @Override
    public boolean updatePassword(User user, String password) throws DBUsernameNotFoundException, PasswordErrorException {
        User usr = this.users.get(user.getUsername());

        if (usr == null) throw new DBUsernameNotFoundException("");

        usr.changePassword(password);

        return true;
    }
}
