package com.sicknasty;

import com.sicknasty.business.AccessPagesTest;
import com.sicknasty.business.AccessPosts;
import com.sicknasty.business.AccessPostsTest;
import com.sicknasty.business.AccessUsers;
import com.sicknasty.business.AccessUsersTest;
import com.sicknasty.objects.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * @author aaron salo
 * this class simply calls all tests from every test class
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccessUsersTest.class,
        AccessPagesTest.class,
        AccessPostsTest.class
})
public class UnitTests
{
}
