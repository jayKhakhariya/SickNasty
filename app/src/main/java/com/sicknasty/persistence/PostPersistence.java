package com.sicknasty.persistence;

import com.sicknasty.objects.Exceptions.NoValidPageException;
import com.sicknasty.objects.Page;
import com.sicknasty.objects.Post;
import com.sicknasty.persistence.exceptions.DBPostIDExistsException;
import com.sicknasty.persistence.exceptions.DBPostIDNotFoundException;

import java.util.ArrayList;

public interface PostPersistence {
    public enum FILTER_BY {TIME_CREATED, AMOUNT_LIKES, AMOUNT_DISLIKES};

    /**
     * Returns a Post specified by it's unique ID.
     * 
     * @param   id the unique ID of the Post
     * @return  the Post corresponding to the ID, otherwise it will return null
     * @throws  DBPostIDNotFoundException thrown when ID is not found
     */
    public Post getPostById(int id) throws DBPostIDNotFoundException, NoValidPageException;

    /**
     * Returns a specified number of Posts shared to a Page (this includes both personal and community Pages).
     * 
     * @param   page the Page to get all the posts from
     * @param   limit the maximum number of posts to fetch
     * @param   filter the filter to sort Page posts by
     * @param   accendingOrder set true to get an ArrayList sorted in accending order
     * @return  ArrayList of type Post (this can be an empty list)
     */
    public ArrayList<Post> getPostsByPage(Page page, int limit, FILTER_BY filter, boolean accendingOrder) throws NoValidPageException;

    /**
     * Inserts a new Post.
     *
     * @param   post the Post object to insert into the database
     * @return  returns true on successful insert, otherwise return false
     * @throws  DBPostIDExistsException this gets thrown if you attempt to insert an existing post
     */
    public boolean insertNewPost(Post post) throws DBPostIDExistsException;

    /**
     * Deletes a Post specified by it's unique ID.
     * 
     * @param   id  the unique ID of the Post
     * @return      returns true on success, otherwise return false
     */
    public boolean deletePost(int id);

    /**
     * Deletes a Post specified by a Post object.
     * 
     * @param   post a Post object to delete
     * @return      returns true on success, otherwise return false
     */
    public boolean deletePost(Post post);
}