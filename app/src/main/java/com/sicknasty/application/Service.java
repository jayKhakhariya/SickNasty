package com.sicknasty.application;
import android.content.Context;
import android.util.Log;

import com.sicknasty.persistence.*;
import com.sicknasty.persistence.sql.PagePersistenceHSQLDB;
import com.sicknasty.persistence.sql.PostPersistenceHSQLDB;
import com.sicknasty.persistence.sql.UserPersistenceHSQLDB;
import com.sicknasty.persistence.stubs.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;


/*******
 * @author aaron
 *
 * this class manages the database, and allows us to fetch the db from other classes with ease
 * We create a new data handler for user, page, and post, which hold the data for each respective
 * items. We then can ask this class to provide the data handlers for each, allowing for persistant
 * data
 */
public class Service {
    private static UserPersistence userData = null;
    private static PostPersistence postData = null;
    private static PagePersistence pageData = null;

    private static String dbPath = "";

    public static synchronized void initDatabase(Context context) {
        Log.d("SQL", "Getting hidden system folder");

        File dir = context.getDir("db", Context.MODE_PRIVATE);
        File dbFile = new File(dir.toString() + "/sicknasty.script");

        Service.dbPath = dbFile.toString();

        Service.registerDriver();
    }

    public static synchronized void initTestDatabase() {
        userData = null;
        postData = null;
        pageData = null;
        try {
            File tmpFile = File.createTempFile("sicknasty-", ".script");

            Service.dbPath = tmpFile.toString();

            Log.d("SQL", "Creating temporary database at: " + tmpFile.toString());
        } catch (IOException e) {
            Log.e("SQL", "Failed to create temporary file");
            Log.e("SQL", e.getMessage());
        }

        Service.registerDriver();
    }

    private static void registerDriver() {
        try {
            Log.d("SQL", "Linking driver class");
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
            Log.d("SQL", "Linked driver class");
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            Log.e("SQL", e.getMessage());
        }
    }

    //this will retrieve the user stub, from which we can call functs such as getUser()
    public static synchronized UserPersistence getUserData() {
        //if the stub hasn't been created yet, create it
        if (userData == null) {
            try {
                userData = new UserPersistenceHSQLDB(Service.dbPath);
            } catch (SQLException e) {
                Log.e("SQL", "Failed to connect to database (user data)");
                Log.e("SQL", e.getSQLState());
                Log.e("SQL", e.getMessage());
                e.printStackTrace();

                userData = new UserPersistenceStub();
            }
        }

        return userData;
    }

    public static synchronized PostPersistence getPostData() {
        if (postData == null) {
            try {
                postData = new PostPersistenceHSQLDB(Service.dbPath);
            } catch (SQLException e) {
                Log.e("SQL", "Failed to connect to database (post data)");
                Log.e("SQL", e.getSQLState());
                Log.e("SQL", e.getMessage());

                postData = new PostPersistenceStub();
            }
        }

        return postData;
    }

    public static synchronized PagePersistence getPageData() {
        if (pageData == null) {
            try {
                pageData = new PagePersistenceHSQLDB(Service.dbPath);
            } catch (SQLException e) {
                Log.e("SQL", "Failed to connect to database (page data)");
                Log.e("SQL", e.getSQLState());
                Log.e("SQL", e.getMessage());

                pageData = new PagePersistenceStub();
            }
        }

        return pageData;
    }
}
