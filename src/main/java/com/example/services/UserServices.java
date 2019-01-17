package com.example.services;

import com.example.controllers.UserController;
import com.example.entities.User;
import com.example.exceptions.ConflictException;
import com.example.exceptions.NotFoundException;
import com.example.exceptions.UnauthorizedException;
import com.example.messaging.BusMessage;
import com.example.messaging.MyFakeMessageBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * This service is used to make CRUD operation on users
 * It also holds a user cache
 */
@Service
public class UserServices
{
    private final Map<String, Map<String, Object>> userFilterableRepository = new HashMap<>();
    private final Map<String, User> userRepository = new HashMap<>();

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    Logger logger = LoggerFactory.getLogger(UserController.class);


    /**
     * Create a user and persist it in a cache
     * @param user the user to create
     * @return the user created
     * or throw a {@link IllegalArgumentException} if a user
     * with a same email has already been created
     */
    public User createUser(User user)
    {
        try
        {
            readWriteLock.writeLock().lock();

            String email = user.getEmail();
            String nickname = user.getNickname();

            if (userRepository.containsKey(email))
            {
                logger.warn("Conflict : The user cannot be created as there is an existing user with the given id ");
                throw new ConflictException("A user with email ["+email+"] has already been created");
            }

            if (!validateNickname(nickname))
            {
                logger.warn("Conflict : The user cannot be created as there is an existing user with the given nickname ");
                throw new ConflictException("A user with nickname ["+nickname+"] has already been created");
            }

            //
            // Save the user in the user cache and filterable cache
            //
            userRepository.put(email, user);
            userFilterableRepository.put(email, user.toMap());

            //
            // Notify the creation of a new user
            //
            BusMessage message = new BusMessage(BusMessage.USER_ADD, user);
            MyFakeMessageBus.getInstance().send(message);

            return user;
        }
        finally
        {
            readWriteLock.writeLock().unlock();
        }
    }

    /**
     * Update a user and persist it in a cache
     * @param user the user to update
     * @return the user updated
     * or throw a {@link IllegalArgumentException} if the user cannot be updated for any reasons
     */
    public User updateUser(User user)
    {
        try
        {
            readWriteLock.writeLock().lock();

            String email = user.getEmail();
            if (!userRepository.containsKey(email))
            {
                logger.warn("Not Found : The user cannot be updated as there is no existing user with the given id ");
                throw new NotFoundException("A user with email ["+email+"] does not exist." +
                        " Therefore, the user cannot be updated");
            }

            //
            // Let's check if the user has a correct password
            //
            if(!validatePassword(user))
            {
                throw new UnauthorizedException("The password is invalid. The user can't be updated");
            }

            //
            // Save the user in the user cache and filterable cache
            //
            userRepository.put(email, user);
            userFilterableRepository.put(email, user.toMap());

            //
            // Notify the creation of a new user
            //
            BusMessage message = new BusMessage(BusMessage.USER_UPDATE, user);
            MyFakeMessageBus.getInstance().send(message);

            return user;
        }
        finally
        {
            readWriteLock.writeLock().unlock();
        }
    }

    /**
     * Get all users matching the given criteria
     * If no criteria is provided, return all users
     * @param criteria an existing criteria to filter on
     * @param value the value of the criteria
     * @return a list of user matching the given criteria
     */
    public List<User> getAllUsers(String criteria, String value)
    {
        try
        {
            readWriteLock.readLock().lock();

            if (criteria == null)
            {
                return userRepository.values().stream()
                        .collect(Collectors.toList());
            }
            else
            {
                List<User> users = new ArrayList<>();
                for(Map.Entry<String, Map<String, Object>> entry : userFilterableRepository.entrySet())
                {

                    Map<String, Object> filterable = entry.getValue();
                    if(filterable.get(criteria).equals(value.toLowerCase()))
                    {
                        users.add(userRepository.get(entry.getKey()));
                    }
                }

                return users;
            }
        }
        finally
        {
            readWriteLock.readLock().unlock();
        }
    }

    /**
     * Get a single user matching the given email
     * @param email the email address of the user
     * @return the user associated to that email
     */
    public User getUser(String email)
    {
        try
        {
            readWriteLock.readLock().lock();

            User user = userRepository.get(email);

            if (user == null)
            {
                throw new NotFoundException("The user with email ["+email+"] does not exist");
            }

            return user;
        }
        finally
        {
            readWriteLock.readLock().unlock();
        }
    }

    /**
     * Delete a user from the cache
     * @param email the key of the user to delete
     * @return true if the user has been deleted
     * Throw an exception if the user cannot be deleted
     */
    public boolean deleteUser(String email)
    {
        try
        {
            readWriteLock.writeLock().lock();

            if (!userRepository.containsKey(email))
            {
                logger.warn("Not found : The user cannot be deleted as there is no existing user with the given id ");
                throw new NotFoundException("A user with email ["+email+"] has not been found" +
                        " and cannot be deleted");
            }

            userFilterableRepository.remove(email);
            User user = userRepository.remove(email);

            //
            // Notify the deletion of a user
            //
            BusMessage message = new BusMessage(BusMessage.USER_DELETE, user);
            MyFakeMessageBus.getInstance().send(message);

            return true;
        }
        finally
        {
            readWriteLock.writeLock().lock();
        }
    }

    public Map<String, User> getUserRepository() {
        return userRepository;
    }

    /**
     * Validate the password
     * Used in case of update, checking that the user is allowed to update its information
     * @param toAuthenticate the user to authenticate
     * @return whether the user is authenticated or not
     */
    private boolean validatePassword(User toAuthenticate)
    {
        User user = userRepository.get(toAuthenticate.getEmail());
        return user.getPassword().equals(toAuthenticate.getPassword());
    }

    /**
     * Validate that the nickname hasn't been used already by some other user
     * @param nickname the nickname to check
     * @return whether the nickname can be used or not
     */
    private boolean validateNickname(String nickname)
    {
        for(Map.Entry<String, User> entry : userRepository.entrySet())
        {
            User user = entry.getValue();
            if(user.getNickname().equals(nickname))
            {
                return false;
            }
        }

        return true;
    }
}
