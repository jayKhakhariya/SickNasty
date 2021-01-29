# Architecture
The architecture for SickNasty contains three layers: the Presentation, Business and Persistence layers.  
  

The Business layer will handle the interaction between the Presentation and Persistence layers. Those two layers will never communicate directly with each other  
Along side the three layers, we also have Domain Specific Objects (DSOs) that get passed around to each of the different layers.  
  
## Presentation Layer
This layer contains three different activities:  
1. PageActivity
    - This activity loads the personal page of the user
2. LoginActivity
    - This activity is the landing zone for new or returning users. Existing users will login here
3. RegistrationActivity
    - This activity is can be launched from the LoginActivity. This is where new users come to create a new account
3. UserAccountActivity
    - This activity will give the user the ability to change their personal details pertaining to their user account
4. adapter/PostAdapter
    - This adapter class provides the translation from the Post object into it's correct view object. This class figures out whether to display an image or video from the Post object
5. CaptionActivity
    - This activity is responsible to for accepting a potential caption from the user after they select media from their device to post
5. SearchActivity
    - This activity allows the user to search for users by username and 
  
## Business
This layer contains three different managing classes that act as the middlemen for Presentation and Persistence:  
1. AccessPages
    - This manager class provides the link between PageActivity and PagePersistence
2. AccessPosts
    - This class provides the link between PageActivity and PostPersistence
3. AccessUsers
    - This class provides the link between the UserPersistence and all three activities  
  
## Persistence
This layer contains three persistence interfaces that save the DSOs and their information:
1. PagePersistence
    - This persistence contains two concrete implementations of this interface:
        - PagePersistenceStub that contains a fake implementation of Pages using a HashMap
        - PagePersistenceHSQLDB that contains calls to the HSQLDB driver that modifies a persistent database
2. PostPersistence
    - As with PagePersistence, there are two concrete implementations:
        - PostPersistenceStub that contains a fake implementation
        - PostPersistenceHSQLDB that contains the real calls to a persistent database
3. UserPersistence
    - Same with the previous two, there are two concrete implementations:
        - UserPersistenceStub that contains fake implementation
        - UserPersistenceHSQLDB that contains real calls to the persistent database  
  
## Diagram  
```
+------------------------------+------------------------------+------------------------------+
|                              |                              |                              |
|         PRESENTATION         |           BUSINESS           |         PERSISTENCE          |
|                              |                              |                              |
+--------------------------------------------------------------------------------------------+
|    +-----------------------------------------+              |                              |
|    |     Login+--------------------------->  v              |  UserPersistence             |
|    |                +------------------>AccessUsers+---------> +->UserPersistenceStub      |
|    |     UserAccount+        |            ^  ^              |  +->UserPersistenceHSQLDB    |
|    |                         |       +----+  |              |                              |
|    |           +---------------------+       |              |                              |
|    |     Search+             |  +------------+              |  PagePersistence             |
|    |             +--------------+       AccessPages+---------> +->PagePersistenceStub      |
|    |             |           |           ^    ^             |  +->PagePersistenceHSQLDB    |
|    |     Register+-----------------------+    |             |                              |
|    |                         |                |             |                              |
|    |        +---------------------------------+             |                              |
|    +----+Page|               |                              |  PostPersistence             |
|    ^         +------------------------->AccessPosts+---------> +->PostPersistenceStub      |
|    |                         |              ^               |  +->PostPersistenceHSQLDB    |
|    +-----Caption+---------------------------+               |                              |
|                              |                              |                              |
+------+-----------------------+------------------------------+------------------------------+
| DSOs |                                                                                     |
+------+       User              Page                         Post                           |
|                                +->CommunityPage                                            |
|                                +->PersonalPage                                             |
|                                                                                            |
|                                                                                            |
|                                                                                            |
+--------------------------------------------------------------------------------------------+
```
