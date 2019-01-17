package com.example.services;

import static org.hamcrest.Matchers.*;

import com.example.entities.User;
import com.example.exceptions.ConflictException;
import com.example.exceptions.NotFoundException;
import com.example.exceptions.UnauthorizedException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class UserServicesTest
{
    private UserServices userServices;

    @Before
    public void setUp()
    {
        userServices = new UserServices();
    }

    @Test(expected = ConflictException.class)
    public void createUser_duplicated_user()
    {
        User user = new User("John", "Smith", "Magic John",
                "john@gmail.com", "qwerty", "France" );


        userServices.createUser(user);

        //
        // Create the user a second time
        // should throw an exception
        //
        userServices.createUser(user);
    }

    @Test(expected = ConflictException.class)
    public void createUser_duplicated_email()
    {
        User user1 = new User("John", "Smith", "Magic John",
                "john@gmail.com", "qwerty", "France" );

        User user2 = new User("John", "Scott", "John is Magic",
                "john@gmail.com", "qwerty", "France" );

        userServices.createUser(user1);

        //
        // Create the user a second time
        // should throw an exception
        //
        userServices.createUser(user2);
    }

    @Test(expected = ConflictException.class)
    public void createUser_duplicated_nickname()
    {
        User user1 = new User("John", "Smith", "Magic John",
                "john@gmail.com", "qwerty", "France" );

        User user2 = new User("Francis", "Smith", "Magic John",
                "francis@gmail.com", "qwerty", "France" );

        userServices.createUser(user1);

        //
        // Create the user a second time
        // should throw an exception
        //
        userServices.createUser(user2);
    }

    @Test
    public void createUser_OK()
    {
        User user1 = new User("John", "Smith", "Magic John",
                "john@gmail.com", "qwerty", "France" );

        User user2 = new User("Francis", "Smith", "Magic Francis",
                "francis@gmail.com", "qwerty", "France" );

        userServices.createUser(user1);
        userServices.createUser(user2);

        Assert.assertThat(userServices.getUserRepository().size(), is(2));
    }


    //
    // Update user tests
    //

    @Test(expected = NotFoundException.class)
    public void updateUser_Non_Existing_User()
    {
        User user1 = new User("John", "Smith", "Magic John",
                "john@gmail.com", "qwerty", "France" );

        userServices.updateUser(user1);
    }

    @Test(expected = UnauthorizedException.class)
    public void updateUser_Password_Invalid()
    {
        User user1 = new User("John", "Smith", "Magic John",
                "john@gmail.com", "qwerty", "France" );

        User user2 = new User("John", "Smith", "Magic John",
                "john@gmail.com", "azerty", "England" );

        userServices.createUser(user1);

        //
        // Update the country, with an invalid password
        // should throw an exception
        //
        userServices.updateUser(user2);
    }

    @Test
    public void updateUser_OK()
    {
        User user1 = new User("John", "Smith", "Magic John",
                "john@gmail.com", "qwerty", "France" );

        User user2 = new User("John", "Scott", "Magic John",
                "john@gmail.com", "qwerty", "England" );

        userServices.createUser(user1);

        //
        // Update the country and lastname
        //
        userServices.updateUser(user2);


        //
        // Get the cached user with email : john@gmail.com
        //
        User cachedUser = userServices.getUserRepository().get("john@gmail.com");
        Assert.assertThat(cachedUser.equals(user2), is(true));
    }

    @Test(expected = NotFoundException.class)
    public void deleteUser_Non_existing_User()
    {
        User user1 = new User("John", "Smith", "Magic John",
                "john@gmail.com", "qwerty", "France" );

        userServices.deleteUser("john@gmail.com");
    }

    @Test
    public void deleteUser_OK()
    {
        User user1 = new User("John", "Smith", "Magic John",
                "john@gmail.com", "qwerty", "France" );

        userServices.createUser(user1);

        //
        // Check that there is one user in the cache
        //
        Assert.assertThat(userServices.getUserRepository().size(), is(1));

        userServices.deleteUser("john@gmail.com");

        //
        // Check that there is no more user in the cache
        //
        Assert.assertThat(userServices.getUserRepository().size(), is(0));
    }

    @Test(expected = NotFoundException.class)
    public void getUser_Non_existing_User()
    {
        userServices.getUser("john@gmail.com");
    }

    @Test
    public void getUser_OK()
    {
        User user1 = new User("John", "Smith", "Magic John",
                "john@gmail.com", "qwerty", "France" );

        userServices.createUser(user1);

        User userFetched = userServices.getUser("john@gmail.com");
        Assert.assertThat(userFetched.equals(user1), is(true));
    }

    @Test
    public void getAllUsers_No_Criteria()
    {
        User user1 = new User("John", "Smith", "Magic John",
                "john@gmail.com", "qwerty", "France" );

        User user2 = new User("Francis", "Smith", "Magic Francis",
                "francis@gmail.com", "qwerty", "France" );

        User user3 = new User("Mike", "Smith", "Magic Mike",
                "mike@gmail.com", "qwerty", "France" );


        userServices.createUser(user1);
        userServices.createUser(user2);
        userServices.createUser(user3);

        //
        // All users should be returned, as no criteria
        //
        List<User> users = userServices.getAllUsers(null, null);
        Assert.assertThat(users.size(), is(3));
    }

    @Test
    public void getAllUsers_Criteria_Country()
    {
        User user1 = new User("John", "Smith", "Magic John",
                "john@gmail.com", "qwerty", "France" );

        User user2 = new User("Francis", "Smith", "Magic Francis",
                "francis@gmail.com", "qwerty", "France" );

        User user3 = new User("Mike", "Smith", "Magic Mike",
                "mike@gmail.com", "qwerty", "England" );


        userServices.createUser(user1);
        userServices.createUser(user2);
        userServices.createUser(user3);

        //
        // Users 1 and 2, should be returned, also the cast of the value doesn't matter
        //
        List<User> users = userServices.getAllUsers("country", "France");
        Assert.assertThat(users.size(), is(2));

        //
        // Get first user and checked that it's John
        //
        User user = users.get(0);
        Assert.assertThat(user.equals(user1), is(true));

        //
        // Get second user and checked that it's Francis
        //
        user = users.get(1);
        Assert.assertThat(user.equals(user2), is(true));

    }

    @Test
    public void getAllUsers_Criteria_Firstname()
    {
        User user1 = new User("John", "Smith", "Magic John",
                "john@gmail.com", "qwerty", "France" );

        User user2 = new User("Francis", "Smith", "Magic Francis",
                "francis@gmail.com", "qwerty", "France" );

        User user3 = new User("John", "Scott", "John Magic",
                "john2@gmail.com", "qwerty", "England" );


        userServices.createUser(user1);
        userServices.createUser(user2);
        userServices.createUser(user3);

        //
        // Users 1 and 3, should be returned, also the cast of the value doesn't matter
        //
        List<User> users = userServices.getAllUsers("firstname", "jOHn");
        Assert.assertThat(users.size(), is(2));

        //
        // Get first user and checked that it's John
        //
        User user = users.get(0);
        Assert.assertThat(user.equals(user1), is(true));

        //
        // Get second user and checked that it's Francis
        //
        user = users.get(1);
        Assert.assertThat(user.equals(user3), is(true));

    }


}
