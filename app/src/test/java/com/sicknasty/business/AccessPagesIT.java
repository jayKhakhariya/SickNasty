package com.sicknasty.business;

import com.sicknasty.application.Service;
import com.sicknasty.objects.Exceptions.ChangeNameException;
import com.sicknasty.objects.Exceptions.ChangeUsernameException;
import com.sicknasty.objects.Exceptions.PasswordErrorException;
import com.sicknasty.objects.Exceptions.UserCreationException;
import com.sicknasty.objects.PersonalPage;
import com.sicknasty.objects.User;
import com.sicknasty.persistence.exceptions.DBPageNameExistsException;
import com.sicknasty.persistence.exceptions.DBPageNameNotFoundException;
import com.sicknasty.persistence.exceptions.DBUsernameExistsException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AccessPagesIT {

    AccessPages pages;
    AccessUsers users;
    User jay;
    PersonalPage page;
    @Before
    public void setUp() throws DBUsernameExistsException,ChangeNameException, PasswordErrorException, UserCreationException, ChangeUsernameException {
        Service.initTestDatabase();
        pages = new AccessPages();
        users=  new AccessUsers();
        jay=new User("Jay K","jay","abcmmdef");
        users.insertUser(jay);
        page=new PersonalPage(jay);

    }

    @Test(expected = DBPageNameExistsException.class)
    public void testInsertPage() throws DBPageNameExistsException, DBPageNameNotFoundException {

        assertTrue("page not added",pages.insertNewPage(page));
        assertEquals("username is different",pages.getPage("jay").getPageName(),"jay");

        assertNotNull("",pages.getPage("jay"));
        page=new PersonalPage(jay);

        assertFalse("two pages with same username added",pages.insertNewPage(page));
        assertTrue("object exist but not deleted",pages.deletePage("jay"));

        assertFalse("object was deleted but found and deleted again",pages.deletePage("jay"));

        assertFalse("object not found but deleted",pages.deletePage("aaron"));



    }
    @Test(expected = DBPageNameNotFoundException.class)
    public void testNullPages() throws DBPageNameExistsException, DBPageNameNotFoundException {
        assertNotNull(pages);
        assertNull("page does not exist",pages.getPage("jay"));


        User user1=null;
        PersonalPage page2 = new PersonalPage(user1);

        assertFalse("null user's page created and added",pages.insertNewPage(page2));

        assertNull("null user's page created and added",pages.getPage(page2.getPageName()));

        assertFalse("object exist but not deleted",pages.deletePage(page2.getPageName()));
    }
    @Test(expected = DBPageNameNotFoundException.class)
    public void testGetPage() throws DBPageNameExistsException, DBPageNameNotFoundException {

        assertNull("page has been somehow added",pages.getPage("jay").getPageName());
        assertFalse("page not added",pages.insertNewPage(page));
        assertNotNull("page is null",pages.getPage("jay").getPageName());
        assertEquals("user's page created but not to corresponding user",pages.getPage("jay").getPageName(),"jay");

        assertTrue("page is null",pages.deletePage("jay"));
    }

    @Test(expected = DBPageNameNotFoundException.class)
    public void testDeletePage() throws DBPageNameExistsException, DBPageNameNotFoundException {
        assertTrue("page not added",pages.insertNewPage(page));
        assertTrue("null user's page created and added",pages.deletePage("jay"));
        assertFalse("already deleted page",pages.deletePage("jay"));
        assertNull("null user's page created and added",pages.getPage("jay"));
    }

    @Test
    public void testDeleteNullPage() throws DBPageNameExistsException {
        assertTrue("page not added",pages.insertNewPage(page));
        assertFalse("page deleted which was not created",pages.deletePage("jayqs"));
        assertTrue("page was somehow not deleted",pages.deletePage("jay"));
    }

    @After
    public void tearDown()
    {
        // reset DB
//        this.tempDB.delete();
    }
}
