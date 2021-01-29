package com.sicknasty.persistence;

import com.sicknasty.objects.Page;
import com.sicknasty.objects.User;
import com.sicknasty.persistence.exceptions.DBPageNameExistsException;
import com.sicknasty.persistence.exceptions.DBPageNameNotFoundException;
import com.sicknasty.persistence.exceptions.DBUserAlreadyFollowingException;

public interface PagePersistence {
    /**
     * Returns a Page specified by it's unique name.
     *
     * @param name the unique name of the Page
     * @return the Page corresponding to the ID, otherwise it will return null
     * @throws DBPageNameNotFoundException thrown when Page name not found
     */
    public Page getPage(String name) throws DBPageNameNotFoundException;

    /**
     * Inserts a new Page.
     *
     * @return returns true on success, otherwise return false
     */
    public boolean insertNewPage(Page page) throws DBPageNameExistsException;

    /**
     * Delete a Page by its unique name.
     *
     * @param name the unique name of the Page
     * @return returns true if it deleted successfully, otherwise false
     */
    public boolean deletePage(String name);

    /**
     * Delete a Page by object.
     *
     * @param page the page object to delete
     * @return returns true if it deleted successfully, otherwise false
     */
    public boolean deletePage(Page page);

    /**
     * Adds a follower to a page.
     *
     * @param page the page to add the follower to
     * @param user the user to add
     * @return returns true if added successfully, otherwise false
     */
    public boolean addFollower(Page page, User user) throws DBUserAlreadyFollowingException;

    /**
     * This changes the name of the page.
     *
     * @param    oldName the old name of the page to change
     * @param    newName the new name of the page to set it to
     * @return returns true if the page name was changed successfully
     */
    public boolean changeName(String oldName, String newName);

}