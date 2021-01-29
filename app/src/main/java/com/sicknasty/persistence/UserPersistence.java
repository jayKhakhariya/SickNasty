package com.sicknasty.persistence;

import com.sicknasty.objects.Exceptions.ChangeUsernameException;
import com.sicknasty.objects.Exceptions.PasswordErrorException;
import com.sicknasty.objects.User;
import com.sicknasty.persistence.exceptions.DBUsernameExistsException;
import com.sicknasty.persistence.exceptions.DBUsernameNotFoundException;

import java.util.ArrayList;

public interface UserPersistence {
    /**
     * This returns a User object specified by its unique Id
     *
     * @param   username the unique username of the User
     * @return  returns the User object if a user with specified username exists, otherwise null
     * @throws  DBUsernameNotFoundException thrown when the username does not exist
     */
    public User getUser(String username) throws DBUsernameNotFoundException;

    /**
     * This returns a String list of all User's usernames
     *
     * @return  returns an ArrayList of Strings
     */
    public ArrayList<String> getAllUsers();

    /**
     * Inserts a new User object
     * 
     * @param   user the User object to insert
     * @return  returns the User object if successful, otherwise null
     * @throws  DBUsernameExistsException thrown when the username already exists
     */
    public User insertNewUser(User user) throws DBUsernameExistsException;

    /**
     * Deletes a User by specifying a User object
     *
     * @param   user the User object to delete
     * @return  returns true if an object was deleted, otherwise false
     */
     public boolean deleteUser(User user);

    /**
     * Updates an existing username
     *
     * @param   oldUsername the old username to update
     * @param   newUsername the new username to replace with
     * @return  true if username was updated, false otherwise
     * @throws  DBUsernameExistsException thrown when new username is already taken
     */
     public boolean updateUsername(String oldUsername, String newUsername) throws DBUsernameExistsException, DBUsernameNotFoundException, ChangeUsernameException;

    /**
     * Changes the password of a user
     *
     * @param   password the new password to change to
     * @return  true if changed successfully
     * @throws  DBUsernameNotFoundException thrown when user does not exist
     * @throws  PasswordErrorException thrown when password requirements are not met
     */
     public boolean updatePassword(User user, String password) throws DBUsernameNotFoundException, PasswordErrorException;
}
