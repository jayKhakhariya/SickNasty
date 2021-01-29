package com.sicknasty.persistence.sql;

import com.sicknasty.objects.Exceptions.NoValidPageException;
import com.sicknasty.objects.Page;
import com.sicknasty.objects.Post;
import com.sicknasty.persistence.PostPersistence;
import com.sicknasty.persistence.exceptions.DBGenericException;
import com.sicknasty.persistence.exceptions.DBPageNameNotFoundException;
import com.sicknasty.persistence.exceptions.DBPostIDExistsException;
import com.sicknasty.persistence.exceptions.DBPostIDNotFoundException;
import com.sicknasty.persistence.exceptions.DBUsernameNotFoundException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PostPersistenceHSQLDB implements PostPersistence {
    private String path;

    public PostPersistenceHSQLDB(String path) throws SQLException {
        this.path = path;

        HSQLDBInitializer.setupTables(this.getConnection());
    }

    /**
     * This will create a new Connection to the database. Once this object leaves scope, it will shutdown
     *
     * @return Connection to the database
     * @throws SQLException
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + this.path + ";shutdown=true", "SA", "");
    }
    @Override
    public Post getPostById(int id) throws DBPostIDNotFoundException, NoValidPageException {
        try {
            // start connection
            Connection db = this.getConnection();

            // get posts
            PreparedStatement stmt = db.prepareStatement(
                "SELECT * FROM Posts WHERE p_id = ? LIMIT 1"
            );
            stmt.setInt(1, id);

            ResultSet result = stmt.executeQuery();

            // send the result to post build (this is a private function that creates the correct subclass)
            if (result.next()) {
                // i also need to get the page this post belongs to
                stmt = db.prepareStatement(
                        "SELECT * FROM PagePosts WHERE p_id = ? LIMIT 1"
                );
                stmt.setInt(1, id);

                // this will always return a row
                ResultSet pageRow = stmt.executeQuery();
                pageRow.next();

                String pageName = pageRow.getString("pg_name");

                PagePersistenceHSQLDB pgSQL = new PagePersistenceHSQLDB(this.path);

                return this.postBuilder(result, pgSQL.getPage(pageName));
            }
        } catch (SQLException | DBUsernameNotFoundException | DBPageNameNotFoundException e) {
            throw new DBGenericException(e);
        }

        throw new DBPostIDNotFoundException(id);
    }

    @Override
    public ArrayList<Post> getPostsByPage(Page page, int limit, FILTER_BY filter, boolean accendingOrder) throws NoValidPageException{
        try {
            // start connection
            Connection db = this.getConnection();

            // SQL arg <-> interface enum
            // this will parse the enum to create SQL parameter
            String filterArg = "";
            switch (filter) {
                case TIME_CREATED:
                    filterArg = "time_created";
                    break;
                case AMOUNT_LIKES:
                    filterArg = "likes";
                    break;
                case AMOUNT_DISLIKES:
                    filterArg = "dislikes";
                    break;
            }

            // begin SELECT statement
            // here i am first querying PagePosts relation to find ALL posts made by a page
            // then it passes the result to the outer SELECT to find them in the Posts table
            // it also orders by the filter above, ascending/descending order and a maximum number
            PreparedStatement stmt = db.prepareStatement(
                "SELECT * FROM Posts " + 
                "WHERE Posts.p_id IN " +
                    "(SELECT p_id FROM PagePosts WHERE PagePosts.pg_name = ?) " + 
                "ORDER BY " + filterArg + " " + (accendingOrder ? "ASC" : "DESC") + " LIMIT ?"
            );
            stmt.setString(1, page.getPageName());
            stmt.setInt(2, limit);

            // prepare the return list
            // order of elements in this list is the same as the query above
            ArrayList<Post> retList = new ArrayList<Post>();
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                try {
                    retList.add(this.postBuilder(result, page));
                } catch (NoValidPageException e){
                    throw e;
                }
            }

            return retList;
        } catch (SQLException | DBUsernameNotFoundException e) {
            throw new DBGenericException(e);
        }
    }

    @Override
    public boolean insertNewPost(Post post) throws DBPostIDExistsException {
        try {
            // get connection
            Connection db = this.getConnection();

            // this is only here as a precaution in case someone does something dumb and attempts to insert an existing post
            // do note that if you, a developer in this group project, are tripping a "dupe post exception", you are entirely at fault.
            // ¯\_(ツ)_/¯ -Lucas
            PreparedStatement stmt = db.prepareStatement(
                "SELECT p_id FROM Posts WHERE p_id = ? LIMIT 1"
            );
            stmt.setInt(1, post.getPostID());

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                throw new DBPostIDExistsException(post.getPostID());
            } else {
                // insert the new post into the DB
                // first value is NULL since that is the autoincrement primary key

                // i also want the key the DB assigned to this post to insert it into the PagePosts relationship
                // i can also use the key to update the Post object
                stmt = db.prepareStatement(
                    "INSERT INTO Posts VALUES(NULL, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                );
                stmt.setString(1, post.getText());
                stmt.setString(2, post.getPath());
                stmt.setInt(3, post.getNumberOfLikes());
                stmt.setInt(4, post.getNumberOfDislikes());
                stmt.setString(5, post.getUserId().getUsername());
                stmt.setLong(6, post.getTimeCreated());
                stmt.executeUpdate();

                // after executing, get the key
                ResultSet postKey = stmt.getGeneratedKeys();
                // there will always be a key (unless SQLException is thrown)
                postKey.next();

                int postID = postKey.getInt(1);

                post.setPostID(postID);

                // insert into the relation
                stmt = db.prepareStatement(
                    "INSERT INTO PagePosts VALUES(?, ?)"
                );
                stmt.setInt(1, postID);
                stmt.setString(2, post.getPageId().getPageName());
                stmt.execute();

                return true;
            }
        } catch (SQLException e) {
            throw new DBGenericException(e);
        }
    }

    @Override
    public boolean deletePost(int id) {
        try {
            // your standard DELETE query
            Connection db = this.getConnection();

            PreparedStatement stmt = db.prepareStatement(
                "DELETE FROM Posts WHERE p_id = ? LIMIT 1"
            );
            stmt.setInt(1, id);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DBGenericException(e);
        }
    }

    @Override
    public boolean deletePost(Post post) {
        // just call the function above
        // i think this is here for "ease of access"
        return this.deletePost(post.getPostID());
    }
    
    private Post postBuilder(ResultSet result, Page page) throws SQLException, DBUsernameNotFoundException, NoValidPageException {
        // this needs to be redone
        UserPersistenceHSQLDB uSQL = new UserPersistenceHSQLDB(this.path);

        Post returnPost = new Post(
            result.getString("text"),
            uSQL.getUser(result.getString("creator_username")),
            result.getString("media_path"),
            result.getInt("likes"),
            result.getInt("dislikes"),
            page
        );

        returnPost.setPostID(result.getInt("p_id"));
        returnPost.setTimeCreated(result.getLong("time_created"));

        return returnPost;
    }
}
