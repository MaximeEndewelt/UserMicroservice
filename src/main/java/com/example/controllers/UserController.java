package com.example.controllers;

import com.example.entities.Criteria;
import com.example.entities.User;
import com.example.exceptions.ExceptionConverter;
import com.example.services.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Rest Controller responsible for actions on user
 */
@RequestMapping("/user")
@RestController
public class UserController
{
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserServices userServices;

    public UserController(UserServices userServices)
    {
        this.userServices = userServices;
    }

    /**
     * A POST endpoint creating a new user
     * @param user the {@link User} to be created
     * @return a OK REQUEST (200) with the user created
     * In case of incomplete information, a BAD REQUEST (400) with
     * the missing information will be sent
     */
    @RequestMapping(value = "/create", method= RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody User user)
    {
        try
        {
            validateUser(user);
            User userCreated = userServices.createUser(user);

            logger.info("The user with email ["+user.getEmail()+"] has been created");
            return ResponseEntity.status(HttpStatus.OK).body(userCreated);
        }
        catch (Exception exception)
        {
            return ExceptionConverter.convertException(exception);
        }
    }

    /**
     * A POST endpoint updating an existing
     * @param user the {@link User} to be updated
     * @return a OK REQUEST (200) with the user updated
     * In case of incomplete information, a BAD REQUEST (400) with
     * the missing information will be sent
     */
    @RequestMapping(value = "/update", method= RequestMethod.POST)
    public ResponseEntity<?> updateUser(@RequestBody User user)
    {
        try
        {
            validateUser(user);
            User userUpdated = userServices.updateUser(user);

            logger.info("The user with email ["+user.getEmail()+"] has been updated");
            return ResponseEntity.status(HttpStatus.OK).body(userUpdated);
        }
        catch (Exception exception)
        {
            return ExceptionConverter.convertException(exception);
        }
    }

    /**
     * A GET endpoint returning all users matching a given criteria
     * @param criteria
     * @param value
     * @return a OK REQUEST (200) with a list of users matching the filter
     * In case of incomplete information, a BAD REQUEST (400) with
     * the missing information will be sent
     */
    @RequestMapping(value = "/getUsers", method= RequestMethod.GET)
    public ResponseEntity<?> getUsers(@RequestParam("criteria")  String criteria, @RequestParam("value") String value)
    {
        try
        {
            validateCriteria(criteria, value);
            List<User> users = userServices.getAllUsers(criteria, value);

            logger.info("The search has been successful");
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }
        catch (Exception exception)
        {
            return ExceptionConverter.convertException(exception);
        }
    }

    /**
     * A GET endpoint returning all users
     * @return a OK REQUEST (200) with a list of users matching the filter
     * In case of incomplete information, a BAD REQUEST (400) with
     * the missing information will be sent
     */
    @RequestMapping(value = "/getAll", method= RequestMethod.GET)
    public ResponseEntity<?> getAll()
    {
        try
        {
            List<User> users = userServices.getAllUsers(null, null);

            logger.info("The search has been successful");
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }
        catch (Exception exception)
        {
            return ExceptionConverter.convertException(exception);
        }
    }

    /**
     * A GET endpoint returning a single user with the given email
     * @param email
     * @return a OK REQUEST (200) with a user matching the email
     * In case of incomplete information, a BAD REQUEST (400) with
     * the missing information will be sent
     */
    @RequestMapping(value = "/{email}", method= RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable String email)
    {
        try
        {
            validateEmail(email);
            User user = userServices.getUser(email);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        catch (Exception exception)
        {
            return ExceptionConverter.convertException(exception);
        }
    }

    /**
     * A DELETE endpoint returning whether a user has been deleted or not
     * @param email the email used as a key to delete the user
     * @return a OK REQUEST (200) if the user is deleted
     * In case of incomplete information, a BAD REQUEST (400) with
     * the missing information will be sent
     */
    @RequestMapping(value = "/{email}", method= RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable String email)
    {
        try
        {
            validateEmail(email);
            boolean removed = userServices.deleteUser(email);

            logger.info("The user with email ["+email+"] has been deleted");
            return ResponseEntity.status(HttpStatus.OK).body(removed);
        }
        catch (Exception exception)
        {
            return ExceptionConverter.convertException(exception);
        }
    }

    /**
     * Check if all required information is given
     * This is only a input validation, not a business one
     * @param user
     * Throw an exception if any required information is missing
     */
    private void validateUser(User user)
    {
        //
        // Very dummy validation
        // Testing all member fields one after another
        //
        String firstname = user.getFirstname();
        String lastname = user.getLastname();
        String nickname = user.getNickname();
        String email = user.getEmail();
        String password = user.getPassword();
        String country = user.getCountry();

        if (firstname == null || firstname.isEmpty())
        {
            throw new IllegalArgumentException("First name can not be validated");
        }

        if (lastname == null || lastname.isEmpty())
        {
            throw new IllegalArgumentException("Last name can not be validated");
        }

        if (nickname == null || nickname.isEmpty())
        {
            throw new IllegalArgumentException("Nick name can not be validated");
        }

        if (email == null || email.isEmpty())
        {
            throw new IllegalArgumentException("Email name can not be validated");
        }
        validateEmail(email);

        if (password == null || password.isEmpty())
        {
            throw new IllegalArgumentException("Password name can not be validated");
        }

        if (country == null || country.isEmpty())
        {
            throw new IllegalArgumentException("Country name can not be validated");
        }
    }


    /**
     * Check if all required information is given
     * This is only a input validation, not a business one
     * @param criteria
     * @param  value
     * Throw an exception if any required information is missing
     */
    private void validateCriteria(String criteria, String value)
    {
        System.out.println("Value : "+value);
        Criteria enumCriteria = Criteria.fromString(criteria);
        if(enumCriteria.equals(Criteria.UNDEFINED) && criteria != null)
        {
            throw new IllegalArgumentException("The given criteria ["+criteria+"] does not correspond " +
                    "to any possible filter");
        }
        else if(!enumCriteria.equals(Criteria.UNDEFINED) && ( (value == null) || value.isEmpty() ))
        {
            throw new IllegalArgumentException("The given criteria ["+criteria+"] must be given with a correct " +
                    "value ["+value+"]");
        }
    }

    private void validateEmail(String email)
    {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        if(! matcher.find())
        {
            throw new IllegalArgumentException("The email address ["+email+"] is not correct");
        }
    }
}
