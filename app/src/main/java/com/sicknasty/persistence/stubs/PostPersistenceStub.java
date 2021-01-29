package com.sicknasty.persistence.stubs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sicknasty.objects.Page;
import com.sicknasty.objects.Post;
import com.sicknasty.persistence.PostPersistence;
import com.sicknasty.persistence.exceptions.DBPostIDExistsException;
import com.sicknasty.persistence.exceptions.DBPostIDNotFoundException;

public class PostPersistenceStub implements PostPersistence {
    // an HashMap containing ALL the posts in the app
    private HashMap<Integer, Post> posts;

    public PostPersistenceStub() {
        this.posts = new HashMap<Integer, Post>();
    }

    @Override
    public Post getPostById(int id) throws DBPostIDNotFoundException {
        if (!this.posts.containsKey(id)) throw new DBPostIDNotFoundException(id);

        return this.posts.get(id);
    }

    @Override
    public ArrayList<Post> getPostsByPage(Page page, int limit, FILTER_BY filter, boolean accendingOrder) {
        if (page == null) return null;

        ArrayList<Post> pagePosts = new ArrayList<Post>();

        for (Map.Entry<Integer, Post> e : this.posts.entrySet()) {
            Post post = e.getValue();

            if (post.getPageId().getPageName() == page.getPageName()) {
                pagePosts.add(post);
            }
        }

        return pagePosts;
    }

    @Override
    public boolean insertNewPost(Post post) throws DBPostIDExistsException {
        if (post == null) return false;

        Post exisitingPost = this.posts.get(post.getPostID());

        if (exisitingPost == null) {
            this.posts.put(post.getPostID(), post);

            return true;
        } else {
            throw new DBPostIDExistsException(post.getPostID());
        }
    }

    @Override
    public boolean deletePost(int id) {
        // remove the post, if it removes then result will be the Post object
        // if it did not fine an id, it will return null
        Post result = this.posts.remove(id);

        return result != null;
    }

    @Override
    public boolean deletePost(Post post) {
        if (post == null) return false;

        Post exisitingPost = this.posts.get(post.getPostID());

        if (exisitingPost == null) {
            return false;
        }

        // i dont like this.
        // the existance of this function can be discussed
        return this.deletePost(exisitingPost.getPostID());
    }
}