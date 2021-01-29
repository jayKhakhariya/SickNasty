package com.sicknasty.business;

import com.sicknasty.objects.Exceptions.ChangeNameException;
import com.sicknasty.objects.Exceptions.ChangeUsernameException;
import com.sicknasty.objects.Exceptions.PasswordErrorException;
import com.sicknasty.objects.Exceptions.UserCreationException;
import com.sicknasty.objects.PersonalPage;
import com.sicknasty.objects.User;
import com.sicknasty.persistence.PagePersistence;
import com.sicknasty.persistence.exceptions.DBPageNameExistsException;
import com.sicknasty.persistence.exceptions.DBPageNameNotFoundException;
import com.sicknasty.persistence.stubs.PagePersistenceStub;

import static org.junit.Assert.*;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class AccessPagesTest {

    PagePersistence pages=new PagePersistenceStub();

    @Test
    public void testInsertPage() throws ChangeNameException, PasswordErrorException, UserCreationException, ChangeUsernameException, DBPageNameExistsException, DBPageNameNotFoundException {
        User jay=new User("Jay K","jay","abcmmdef");

            PersonalPage page = new PersonalPage(jay);

            assertTrue("page not added", pages.insertNewPage(page));
            assertEquals("username is different", pages.getPage("jay").getPageName(), "jay");

            assertNotNull("", pages.getPage("jay"));


        assertTrue("object exist but not deleted",pages.deletePage("jay"));
        try {
            assertFalse("object not found but deleted", pages.deletePage("jay"));

            assertFalse("object not found but deleted", pages.deletePage("aaron"));
        } catch (Exception e ){
            System.out.println(e.getMessage());
            fail();
        }
    }

    //this test is 100% useless. WILL DELETE
    @Test
    public void testNullPages() throws DBPageNameExistsException, DBPageNameNotFoundException {
        PersonalPage page1 =null;

        assertFalse("page not added",pages.insertNewPage(page1));

        User user=null;
        PersonalPage page2 = new PersonalPage(user);

        assertNull("null user's page created and added",pages.getPage(page2.getPageName()));

        assertFalse("object exist but not deleted",pages.deletePage(page2.getPageName()));
    }
}
