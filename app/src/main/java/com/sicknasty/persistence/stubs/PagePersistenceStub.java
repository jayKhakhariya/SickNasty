package com.sicknasty.persistence.stubs;

import java.util.ArrayList;
import java.util.HashMap;

import com.sicknasty.objects.User;
import com.sicknasty.persistence.PagePersistence;
import com.sicknasty.objects.Page;
import com.sicknasty.persistence.exceptions.DBPageNameExistsException;
import com.sicknasty.persistence.exceptions.DBPageNameNotFoundException;
import com.sicknasty.persistence.exceptions.DBUserAlreadyFollowingException;

public class PagePersistenceStub implements PagePersistence {
    private HashMap<String, Page> pages;
    private HashMap<String, ArrayList<User>> followers;

    public PagePersistenceStub() {
        this.pages = new HashMap<String, Page>();
        this.followers = new HashMap<String, ArrayList<User>>();
    }

    @Override
    public Page getPage(String name) throws DBPageNameNotFoundException {
        if (name == null) return null;

        if (!this.pages.containsKey(name)) throw new DBPageNameNotFoundException(name);

        return this.pages.get(name);
    }

    @Override
    public boolean insertNewPage(Page page) throws DBPageNameExistsException {
        if (page == null) return false;

        String pageName = page.getPageName();

        if (pages.containsKey(pageName)) throw new DBPageNameExistsException(page.getPageName());

        this.pages.put(pageName, page);

        return true;
    }

    @Override
    public boolean deletePage(String name) {
        if (name == null) return false;

        Page result = this.pages.remove(name);

        return result != null;
    }

    @Override
    public boolean deletePage(Page page) {
        if (page == null) return false;

        Page exisitingPost = this.pages.get(page.getPageName());

        if (exisitingPost == null) {
            return false;
        }

        // i dont like this.
        // the existance of this function can be discussed
        return this.deletePage(exisitingPost.getPageName());
    }

    @Override
    public boolean addFollower(Page page, User user) throws DBUserAlreadyFollowingException {
        if (page == null) return false;

        ArrayList<User> localFollowers = this.followers.get(page.getPageID());

        if (localFollowers == null) {
            localFollowers = new ArrayList<User>();

            this.followers.put(page.getPageName(), localFollowers);
        }

        if (localFollowers.contains(user))
            throw new DBUserAlreadyFollowingException(user.getUsername(), page.getPageName());

        localFollowers.add(user);

        return true;
    }

	@Override
	public boolean changeName(String oldName, String newName) {
		if (this.pages.containsKey(oldName)) {
			Page oldPage = this.pages.get(oldName);
			oldPage.changePageName(newName);

			this.pages.remove(oldName);	
			this.pages.put(newName, oldPage);	

			return true;
		}

		return false;
	}
}
