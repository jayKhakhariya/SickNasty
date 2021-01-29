package com.sicknasty.business;

import com.sicknasty.application.Service;
import com.sicknasty.objects.Exceptions.PasswordErrorException;
import com.sicknasty.objects.Exceptions.UserNotFoundException;
import com.sicknasty.objects.User;
import com.sicknasty.persistence.UserPersistence;
import com.sicknasty.persistence.exceptions.DBUsernameExistsException;
import com.sicknasty.persistence.exceptions.DBUsernameNotFoundException;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccessUsersIT {

    AccessUsers users;

    @Before public void setUp() {
        Service.initTestDatabase();
        users = new AccessUsers();              //use business layer
    }

    @Test
    public void testInsertUsers() {

        try {
            User newUser = new User("Jay K", "jay", "23416772");
            users.insertUser(newUser);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test(expected = DBUsernameNotFoundException.class)
    public void testDeleteUsers() throws DBUsernameNotFoundException{
        String username = "jay";
        try {
            User newUser = new User("Jay K", username, "23416772");
            users.insertUser(newUser);
            users.deleteUser(username);
        } catch (Exception e) {
            fail();
        }

        try {
            users.getUser(username); //should throw an exception
        } catch (DBUsernameNotFoundException e) {
            throw e;
        } catch (UserNotFoundException e) {
            fail();
        }
    }

    @Test(expected = DBUsernameExistsException.class)
    public void testUsernameExistsException() throws DBUsernameExistsException {

        try {
            User newUser = new User("Jay K", "jay", "23416772");
            User sameUser = new User("Jay K", "jay", "23416772");
            users.insertUser(newUser);
            users.insertUser(sameUser);
        } catch (DBUsernameExistsException e) {
            throw new DBUsernameExistsException("Username already Exists");
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testUpdatesInUsername(){
        String username = "jay";
        String newUsername = "aaron";
        try {
            User user1 = new User("Jay K",username,"abcmmdef");
            users.insertUser(user1);
            users.updateUsername(user1, newUsername);
            user1 = users.getUser(newUsername);
            assert(user1.getUsername() != username);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test (expected = PasswordErrorException.class)
    public void testPasswordError() throws PasswordErrorException{
        String goodPass = "thisIsAGoodPass1";
        String badPass = "bad";
        try {
            User aaron = new User("A Salo", "aaron", goodPass);
        } catch (Exception e) {
            fail();
        }
        try {
            //create a user with a bad password
            User aaron = new User("A Salo", "aaron", badPass);
        } catch (PasswordErrorException e) {
            throw new PasswordErrorException("Bad password");
        } catch (Exception e) {
            fail();
        }
    }

    @Test (expected = PasswordErrorException.class)
    public void testPasswordChangeError() throws PasswordErrorException{
        String goodPass = "thisIsAGoodPass1";
        String badPass = "bad";
        try {
            //add a good pass, then change it to a bad pass through the database
            User aaron = new User("A Salo", "aaron", goodPass);
            users.insertUser(aaron);
            users.updateUserPassword("aaron", badPass);
        } catch (PasswordErrorException e) {
            throw new PasswordErrorException("bad password");
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testUpdatesInPassword(){
        try {
            String username = "jay";
            User jay = new User("Jay K", username, "abcmmdef");
            users.insertUser(jay);

            users.updateUserPassword( username,  "234567819");
            jay = users.getUser(username);
            assertFalse("password is still the old password", jay.checkPasswordCorrect("abcmmdef"));
            assertTrue("the new password didnt work", jay.checkPasswordCorrect("234567819"));
        } catch (Exception e) {
            fail();
        }
    }


}
