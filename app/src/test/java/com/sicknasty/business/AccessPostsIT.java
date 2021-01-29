package com.sicknasty.business;

import com.sicknasty.application.Service;
import com.sicknasty.objects.Exceptions.ChangeNameException;
import com.sicknasty.objects.Exceptions.ChangeUsernameException;
import com.sicknasty.objects.Exceptions.NoValidPageException;
import com.sicknasty.objects.Exceptions.PasswordErrorException;
import com.sicknasty.objects.Exceptions.UserCreationException;
import com.sicknasty.objects.Exceptions.UserNotFoundException;
import com.sicknasty.objects.PersonalPage;
import com.sicknasty.objects.Post;
import com.sicknasty.objects.User;
import com.sicknasty.persistence.exceptions.DBPageNameExistsException;
import com.sicknasty.persistence.exceptions.DBPostIDExistsException;
import com.sicknasty.persistence.exceptions.DBUsernameExistsException;
import com.sicknasty.persistence.exceptions.DBUsernameNotFoundException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class AccessPostsIT {

    private AccessPosts posts;
    private AccessUsers users;
    private AccessPages pages;
    @Before
    public void setUp()
    {

        Service.initTestDatabase();
        //set up the db
        posts=new AccessPosts();
        users=new AccessUsers();
        pages=new AccessPages();
    }
    @Test
    public void testInsertPost() throws UserNotFoundException, NoValidPageException, DBUsernameNotFoundException,ChangeNameException, PasswordErrorException, UserCreationException, ChangeUsernameException, DBPostIDExistsException, DBUsernameExistsException, DBPageNameExistsException {

        System.out.println("Starting insertPostTest::");
        User user1=new User("Jay K","jay1","1234567");
        users.insertUser(user1);
        pages.insertNewPage(user1.getPersonalPage());

        Post newPost = new Post("Caption is nice", user1, "some random path doesn't matter", 0, 0, user1.getPersonalPage());

        assertTrue(posts.insertPost(newPost));
        assertEquals(newPost.getNumberOfDislikes(),0);
        assertEquals(newPost.getNumberOfLikes(),0);
        assertEquals(newPost.getPageId(),user1.getPersonalPage());
        assertEquals(newPost.getPath(),"some random path doesn't matter");
        assertEquals(newPost.getText(),"Caption is nice");
        assertEquals(newPost.getUserId(),user1);

        assertTrue(posts.deletePost(newPost));
        users.deleteUser("jay1");
        System.out.println("Finished insertPostTest");
    }

    @Test(expected = DBPostIDExistsException.class)
    public void testDuplicatePost() throws NoValidPageException, ChangeNameException, PasswordErrorException, UserCreationException, ChangeUsernameException, DBPostIDExistsException, DBUsernameExistsException, DBPageNameExistsException {
        System.out.println("Starting duplicatePostTest::");
        User user1=new User("Jay K","jay1","1234567");
        Post newPost=new Post("Caption is nice",user1,"some random path doesn't matter",0,0,user1.getPersonalPage());

        users.insertUser(user1);
        pages.insertNewPage(user1.getPersonalPage());

        assertTrue(posts.insertPost(newPost));
        assertFalse(posts.insertPost(newPost));     //it is adding same post twice

        assertTrue(posts.deletePost(newPost.getPostID()));
        System.out.println("Finished duplicatePostTest");

    }
    @Test
    public void testGetPost() throws NoValidPageException, ChangeNameException, PasswordErrorException, UserCreationException, ChangeUsernameException, DBPostIDExistsException, DBPageNameExistsException, DBUsernameExistsException {
        User user1=new User("Jay K","jay1","1234567");
        PersonalPage page = new PersonalPage(user1);

        users.insertUser(user1);
        pages.insertNewPage(user1.getPersonalPage());
        Post newPost=new Post("Caption is nice",user1,"some random path doesn't matter",0,0,user1.getPersonalPage());

        assertTrue(posts.insertPost(newPost));
        assertEquals(posts.getPostsByPage(page).size(),1);
        assertEquals(posts.getPostsByPage(page).get(0).getPostID(),newPost.getPostID());

        assertTrue(posts.deletePost(newPost));

        System.out.println("Finished testGetPost");
    }
    @Test
    public void testRemovePost() throws NoValidPageException,ChangeNameException, PasswordErrorException, UserCreationException, ChangeUsernameException, DBPostIDExistsException, DBUsernameExistsException, DBPageNameExistsException {
        System.out.println("Started testRemovePost");

        User user1=new User("Jay K","jay1","1234567");
        PersonalPage page = new PersonalPage(user1);

        users.insertUser(user1);
        pages.insertNewPage(user1.getPersonalPage());

        Post newPost=new Post("Caption is nice",user1,"some random path doesn't matter",0,0,user1.getPersonalPage());

        assertTrue(posts.insertPost(newPost));
        assertTrue(posts.deletePost(newPost));

        assertFalse(posts.getPostsByPage(page).remove(newPost));

        System.out.println("Finished testRemovePost");
    }
    @Test
    public void testNotExistPost() throws NoValidPageException,ChangeNameException, PasswordErrorException, UserCreationException, ChangeUsernameException, DBPostIDExistsException{
        System.out.println("Started testNotExist");

        User user1=new User("Jay K","jay1","1234567");
        PersonalPage page = new PersonalPage(user1);

        Post newPost=new Post("Caption is nice",user1,"some random path doesn't matter",0,0,page);

        assertFalse(posts.getPostsByPage(page).contains(newPost));
        assertFalse(posts.deletePost(newPost));

        System.out.println("Finished testNotExistPost");

    }

    @Test
    public void testDeleteById() throws NoValidPageException,ChangeNameException, PasswordErrorException, UserCreationException, ChangeUsernameException, DBPostIDExistsException, DBUsernameExistsException, DBPageNameExistsException {
        System.out.println("Started testDeleteById");
        User user1=new User("Jay K","jay1","1234567");
        PersonalPage page = new PersonalPage(user1);

        users.insertUser(user1);
        pages.insertNewPage(user1.getPersonalPage());

        Post newPost=new Post("Caption is nice",user1,"some random path doesn't matter",0,0,page);

        assertTrue(posts.insertPost(newPost));
        assertTrue(posts.deletePost(newPost.getPostID()));

        assertFalse(posts.getPostsByPage(page).contains(newPost));

        System.out.println("Finished testDeleteById");

    }

    @After
    public void tearDown(){
        //destroy database file
    }


}
