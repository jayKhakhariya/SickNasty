package com.sicknasty.persistence.sql;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// no, I did not forget to include a keyword as a part of the class signature
// blame Java for being stupid
// - Lucas
class HSQLDBInitializer {
    static void setupTables(Connection db) throws SQLException {
        Log.d("INNER SQL", "Setting up tables");

        PreparedStatement stmt = db.prepareStatement(
            "CREATE TABLE IF NOT EXISTS Users (" +
                "username VARCHAR(32) PRIMARY KEY," +
                "name VARCHAR(32) NOT NULL," +
                "password VARCHAR(128) NOT NULL" +
            ")"
        );
        stmt.execute();
        
        stmt = db.prepareStatement(
            "CREATE TABLE IF NOT EXISTS Posts (" +
                "p_id INTEGER IDENTITY PRIMARY KEY," +
                "text VARCHAR(1024) NOT NULL," +
                "media_path VARCHAR(1024)," +
                "likes INTEGER NOT NULL," +
                "dislikes INTEGER NOT NULL," +
                "creator_username VARCHAR(32) NOT NULL," +
                "time_created BIGINT NOT NULL," +
                "FOREIGN KEY(creator_username) REFERENCES Users(username) ON DELETE CASCADE" +
            ")"
        );
        stmt.execute();
        
        stmt = db.prepareStatement(
            "CREATE TABLE IF NOT EXISTS Pages (" +
                "pg_name VARCHAR(32) NOT NULL PRIMARY KEY," +
                "creator_username VARCHAR(32) NOT NULL," +
                "type TINYINT NOT NULL" +
            ")"
        );
        stmt.execute();
            
        stmt = db.prepareStatement(
            "CREATE TABLE IF NOT EXISTS PagePosts (" +
                "p_id INTEGER NOT NULL," +
                "pg_name VARCHAR(32) NOT NULL," +
                "FOREIGN KEY(p_id) REFERENCES POSTS(p_id) ON DELETE CASCADE," +
                "FOREIGN KEY(pg_name) REFERENCES Pages(pg_name) ON DELETE CASCADE" +
            ")"
        );
        stmt.execute();

        stmt = db.prepareStatement(
            "CREATE TABLE IF NOT EXISTS PageFollowers (" +
                "username VARCHAR(32) NOT NULL," +
                "pg_name VARCHAR(32) NOT NULL," +
                "FOREIGN KEY(username) REFERENCES Users(username) ON DELETE CASCADE," +
                "FOREIGN KEY(pg_name) REFERENCES Pages(pg_name) ON DELETE CASCADE" +
            ")"
        );
        stmt.execute();

        Log.d("INNER SQL", "Finished tables");
    }
}
