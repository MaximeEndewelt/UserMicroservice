package com.example.controllers;

import com.example.entities.User;
import com.example.services.UserServices;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.Matchers.is;

public class UserControllerTest
{
    private UserController userController;

    @Before
    public void setUp()
    {
        userController = new UserController(new UserServices());
    }

    @Test
    public void createUser_Incomplete_Input()
    {
        User user = new User("John", "Smith", "Magic John",
                "john@gmail.com", "qwerty", "" );
        //
        // Call the endpoint with incomplete user object
        //
        ResponseEntity<?> response =  userController.createUser(user);

        Assert.assertThat( response.getStatusCode().equals(HttpStatus.BAD_REQUEST), is(true));

    }

    @Test
    public void createUser_Fake_Email()
    {
        User user = new User("John", "Smith", "Magic John",
                "notAnEmail.com", "qwerty", "France" );
        //
        // Call the endpoint with incomplete user object
        //
        ResponseEntity<?> response =  userController.createUser(user);
        Assert.assertThat( response.getStatusCode().equals(HttpStatus.BAD_REQUEST), is(true));
    }

    @Test
    public void createUser_OK()
    {
        User user = new User("John", "Smith", "Magic John",
                "john@gmail.com", "qwerty", "France" );
        //
        // Call the endpoint with incomplete user object
        //
        ResponseEntity<?> response =  userController.createUser(user);
        Assert.assertThat( response.getStatusCode().equals(HttpStatus.OK), is(true));
    }

    @Test
    public void updateUser_Incomplete_Input()
    {
        User user = new User("John", "Smith", "Magic John",
                "john@gmail.com", "qwerty", "" );
        //
        // Call the endpoint with incomplete user object
        //
        ResponseEntity<?> response =  userController.updateUser(user);
        Assert.assertThat( response.getStatusCode().equals(HttpStatus.BAD_REQUEST), is(true));
    }

    @Test
    public void updateUser_OK()
    {
        User user1 = new User("John", "Smith", "Magic John",
                "john@gmail.com", "qwerty", "France" );

        User user2 = new User("John", "Scott", "Magic John",
                "john@gmail.com", "qwerty", "France" );
        //
        // Call the endpoint with incomplete user object
        //
        ResponseEntity<?> response =  userController.createUser(user1);
        Assert.assertThat( response.getStatusCode().equals(HttpStatus.OK), is(true));

        //
        // Update the lastname
        //
        response =  userController.updateUser(user2);
        Assert.assertThat( response.getStatusCode().equals(HttpStatus.OK), is(true));
    }

    @Test
    public void deleteUser_Fake_Email()
    {
        User user = new User("John", "Smith", "Magic John",
                "john@gmail.com", "qwerty", "" );
        //
        // Call the endpoint with incomplete user object
        //
        ResponseEntity<?> response =  userController.deleteUser("dadun----sdjndjcn");
        Assert.assertThat( response.getStatusCode().equals(HttpStatus.BAD_REQUEST), is(true));
    }

    @Test
    public void deleteUser_Non_Existing_User()
    {
        //
        // Call the endpoint with non existing email address
        //
        ResponseEntity<?> response =  userController.deleteUser("john@gmail.com");
        Assert.assertThat( response.getStatusCode().equals(HttpStatus.NOT_FOUND), is(true));
    }

    @Test
    public void deleteUser_OK()
    {
        User user = new User("John", "Smith", "Magic John",
                "john@gmail.com", "qwerty", "France" );
        //
        // Create a user and delete
        //
        userController.createUser(user);
        ResponseEntity<?> response =  userController.deleteUser("john@gmail.com");
        Assert.assertThat( response.getStatusCode().equals(HttpStatus.OK), is(true));
    }
}
