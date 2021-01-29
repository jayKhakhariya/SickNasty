package com.sicknasty.persistence.sql;

import com.sicknasty.objects.CommunityPage;
import com.sicknasty.objects.Page;
import com.sicknasty.objects.PersonalPage;
import com.sicknasty.objects.User;
import com.sicknasty.persistence.PagePersistence;
import com.sicknasty.persistence.UserPersistence;
import com.sicknasty.persistence.exceptions.DBGenericException;
import com.sicknasty.persistence.exceptions.DBPageNameExistsException;
import com.sicknasty.persistence.exceptions.DBPageNameNotFoundException;
import com.sicknasty.persistence.exceptions.DBUserAlreadyFollowingException;
import com.sicknasty.persistence.exceptions.DBUsernameNotFoundException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PagePersistenceHSQLDB implements PagePersistence {
    private final static int PERSONAL_PAGE = 0;
    private final static int COMMUNITY_PAGE = 1;

    private String path;

    public PagePersistenceHSQLDB(String path) throws SQLException {
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
    public Page getPage(String name) throws DBPageNameNotFoundException {
        try {
            // create connection
            Connection db = this.getConnection();

            PreparedStatement stmt = db.prepareStatement(
                "SELECT * FROM Pages WHERE pg_name = ? LIMIT 1"
            );
            stmt.setString(1, name);

            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                // this doesnt feel right but it seems to be fine?
                // query our other class to get the user
                UserPersistence userDB = new UserPersistenceHSQLDB(this.path);

                User user = userDB.getUser(result.getString("creator_username"));

                if (user != null) {
                    // use our private "enum" to create the page
                    switch (result.getInt("type")) {
                        case PERSONAL_PAGE:
                            return new PersonalPage(user);
                        case COMMUNITY_PAGE:
                            return new CommunityPage(result.getString("pg_name"), user);
                    }
                }
            }
        } catch (SQLException | DBUsernameNotFoundException e) {
            throw new DBGenericException(e);
        }

        throw new DBPageNameNotFoundException(name);
    }

    @Override
    public boolean insertNewPage(Page page) throws DBPageNameExistsException {
        try {
            Connection db = this.getConnection();

            // check to see if the page exists first
            PreparedStatement stmt = db.prepareStatement(
                "SELECT pg_name FROM Pages WHERE pg_name = ? LIMIT 1"
            );
            stmt.setString(1, page.getPageName());

            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                throw new DBPageNameExistsException(page.getPageName());
            } else {
                // insert new page
                stmt = db.prepareStatement(
                    "INSERT INTO Pages VALUES(?, ?, ?)"
                );
                stmt.setString(1, page.getPageName());
                stmt.setString(2, page.getCreator().getUsername());

				/*
					So here, I am using instanceof. If we were to not use instanceof, we would
					either have to have two difference tables that contain the same data
					or we would have to keep a variable that keeps track of what kind of Page it is
					in the Page object itself.

					If we decided to have two tables, we would be violating DRY.
					If we had a variable to keep a "magic" value, we would still be dependent on an if/else.
					If we used instanceof, we would have to depend on if/else.

					Either way, we lose.
				*/
                if (page instanceof PersonalPage) {
                    stmt.setInt(3, this.PERSONAL_PAGE);
                } else {
                    stmt.setInt(3, this.COMMUNITY_PAGE);
                }

                stmt.execute();

                return true;
            }
        } catch (SQLException e) {
            throw new DBGenericException(e);
        }
    }

    @Override
    public boolean deletePage(String name) {
        try {
            // deletes a page. :|

            Connection db = this.getConnection();

            PreparedStatement stmt = db.prepareStatement(
                "DELETE FROM Pages WHERE pg_name = ? LIMIT 1"
            );
            stmt.setString(1, name);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DBGenericException(e);
        }
    }

    @Override
    public boolean deletePage(Page page) {
        return this.deletePage(page.getPageName());
    }

    @Override
    public boolean addFollower(Page page, User user) throws DBUserAlreadyFollowingException {
        String pageName = page.getPageName();
        String username = user.getUsername();

        try {
            Connection db = this.getConnection();

            PreparedStatement stmt = db.prepareStatement(
                "SELECT * FROM PageFollowers WHERE username = ? AND pg_name = ? LIMIT 1"
            );
            stmt.setString(1, username);
            stmt.setString(2, pageName);

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                throw new DBUserAlreadyFollowingException(username, pageName);
            } else {
                stmt = db.prepareStatement(
                    "INSERT INTO PageFollowers VALUES (?, ?)"
                );
                stmt.setString(1, username);
                stmt.setString(2, pageName);

                stmt.execute();

                return true;
            }
        } catch (SQLException e) {
            throw new DBGenericException(e);
        }
    }

	@Override
	public boolean changeName(String oldName, String newName) {
		try {
			Connection db = this.getConnection();

			PreparedStatement stmt = db.prepareStatement(
				"UPDATE Pages SET pg_name = ? WHERE pg_name = ? LIMIT 1"
			);
			stmt.setString(1, newName);
			stmt.setString(2, oldName);	

			return stmt.executeUpdate() == 1;
		} catch (SQLException e) {
			throw new DBGenericException(e);
		}
	}
}
