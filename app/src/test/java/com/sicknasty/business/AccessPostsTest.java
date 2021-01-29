package com.sicknasty.business;

import com.sicknasty.objects.Exceptions.ChangeNameException;
import com.sicknasty.objects.Exceptions.ChangeUsernameException;
import com.sicknasty.objects.Exceptions.NoValidPageException;
import com.sicknasty.objects.Exceptions.PasswordErrorException;
import com.sicknasty.objects.Exceptions.UserCreationException;
import com.sicknasty.objects.Page;
import com.sicknasty.objects.PersonalPage;
import com.sicknasty.objects.Post;
import com.sicknasty.objects.User;
import com.sicknasty.persistence.PagePersistence;
import com.sicknasty.persistence.PostPersistence;
import com.sicknasty.persistence.exceptions.DBPostIDExistsException;
import com.sicknasty.persistence.stubs.PostPersistenceStub;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class AccessPostsTest {

    PostPersistence postStub;
    public AccessPostsTest (){
        postStub =  new PostPersistenceStub();
    }

    @Test
    public void testGetPostsByPage() throws NoValidPageException,ChangeNameException, PasswordErrorException, UserCreationException, ChangeUsernameException, DBPostIDExistsException {
        User newUser = new User("hello", "helloo", "hellooooooo");
        Page page = new PersonalPage(newUser);
        Post post = new Post("this is a test",null,null,1, 1, page);
        //insert the post
        assertTrue("the post was not properly inserted", postStub.insertNewPost(post));

        //try searching for the post in a different page that what it was placed in
        Page newPage=new PersonalPage(new User("test", "test", "testing1234"));
        ArrayList<Post> test = postStub.getPostsByPage(newPage,4,null,false);
        assertFalse("the post should not be in this list, it was posted to a different page", test.contains(post));

        //Check if the post is in the page we inserted into's list of posts
        assertTrue("the post is not in the pages list of posts", postStub.getPostsByPage(page,10,null,false).contains(post));
    }

    @Test
    public void testPostInsert() throws NoValidPageException,ChangeNameException, PasswordErrorException, UserCreationException, ChangeUsernameException, DBPostIDExistsException {
        User newUser = new User("hello", "helloo", "hellooooooo");
        Page page = new PersonalPage(newUser);
        Post post = new Post("this is a test",null,null,1, 1, page);
        assertTrue("the post was not properly inserted", postStub.insertNewPost(post));
    }

    @Test
    public void testPostDelete() throws NoValidPageException,ChangeNameException, PasswordErrorException, UserCreationException, ChangeUsernameException, DBPostIDExistsException {
        User newUser = new User("hello", "helloo", "hellooooooo");
        Page page = new PersonalPage(newUser);
        Post post = new Post("this is a test",null,null,1, 1, page);
        assertTrue("the post was not properly inserted", postStub.insertNewPost(post));


        //try deleting the post
        assertTrue("the deletion failed from the persistence stub", postStub.deletePost(post));
        //try deleting the same post again (should fail)
        assertFalse("We somehow deleted the same post twice", postStub.deletePost(post));

        Post aDifferentPost = new Post("this is a test",newUser,null,1, 1, page);
        //try to delete a post that has not been added
        assertFalse("deleted a post that was not inserted to the db", postStub.deletePost(aDifferentPost));
    }


}
