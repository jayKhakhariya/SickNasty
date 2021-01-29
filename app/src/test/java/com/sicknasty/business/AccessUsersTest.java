package com.sicknasty.business;

import com.sicknasty.objects.Exceptions.ChangeNameException;
import com.sicknasty.objects.Exceptions.ChangeUsernameException;
import com.sicknasty.objects.Exceptions.PasswordErrorException;
import com.sicknasty.objects.Exceptions.UserCreationException;
import com.sicknasty.objects.Exceptions.UserNotFoundException;
import com.sicknasty.objects.User;
import com.sicknasty.persistence.UserPersistence;
import com.sicknasty.persistence.exceptions.DBUsernameExistsException;
import com.sicknasty.persistence.exceptions.DBUsernameNotFoundException;
import com.sicknasty.persistence.stubs.UserPersistenceStub;

import org.junit.Test;
import static org.junit.Assert.*;

public class AccessUsersTest {

    UserPersistence userPersistence=new UserPersistenceStub();
    @Test
    public void testInsertUsers() throws ChangeNameException, PasswordErrorException, UserCreationException, ChangeUsernameException, DBUsernameExistsException, DBUsernameNotFoundException {

        User newUser=new User("Jay K","jay1","1234567");
        assertNotNull(userPersistence.insertNewUser(newUser));

        assertNotNull(new User("Aaron Solo","aaron","abcdefg"));


        assertNotNull(userPersistence.insertNewUser(new User("Aaron Solo","aaron","abcdefg")));

        assertTrue("user not deleted",userPersistence.deleteUser(userPersistence.getUser("jay1")));
        assertTrue("user not deleted",userPersistence.deleteUser(userPersistence.getUser("aaron")));

    }
    @Test(expected = DBUsernameExistsException.class)
    public void testDuplicateUsers() throws ChangeNameException, PasswordErrorException, UserCreationException, ChangeUsernameException, DBUsernameExistsException, DBUsernameNotFoundException {
        User newUser=new User("Jay K","jay1","1234567");
        assertNotNull(userPersistence.insertNewUser(newUser));
        assertNull("duplicated  added!!!Error",userPersistence.insertNewUser(new User("Jay K","jay1","abcmmdef")));


        assertFalse("item not found but still deleted!!error",userPersistence.deleteUser(userPersistence.getUser("aaron")));
        assertTrue("existing user not deleted !!error",userPersistence.deleteUser(userPersistence.getUser("jay")));
    }
    @Test(expected = DBUsernameExistsException.class)
    public void testUpdatesInUsername() throws DBUsernameNotFoundException,ChangeNameException, PasswordErrorException, UserCreationException, ChangeUsernameException, DBUsernameExistsException {
        User user1=new User("Jay K","jay","abcmmdef");


        assertNotNull("user not added",userPersistence.insertNewUser(user1));
        assertTrue("username not changed even though it was available",userPersistence.updateUsername("jay","aaron"));

        assertNotNull("nd nad",userPersistence.getUser("aaron"));
        assertNotNull("user not added",userPersistence.insertNewUser(user1));
        assertNull("duplicated  added!!!Error",userPersistence.insertNewUser(new User("Jay K","jay","abcmmdef")));

        assertTrue(" not deleted!!error",userPersistence.deleteUser(userPersistence.getUser("aaron")));
        assertTrue("existing user not deleted !!error",userPersistence.deleteUser(userPersistence.getUser("jay")));
    }

    @Test
    public void testUpdatesInPassword() throws ChangeNameException, PasswordErrorException, UserCreationException, ChangeUsernameException, DBUsernameExistsException, DBUsernameNotFoundException, UserNotFoundException {
        User jay=new User("Jay K","jay","abcmmdef");

        assertNotNull("user not added",userPersistence.insertNewUser(jay));

        userPersistence.updatePassword(jay,"1234556777");
        assertFalse("password not change",jay.checkPasswordCorrect("abcmmdef"));
        assertTrue("password not changed",jay.checkPasswordCorrect("1234556777"));

        userPersistence.deleteUser(jay);
    }
}
