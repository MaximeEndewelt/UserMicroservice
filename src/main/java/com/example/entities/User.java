package com.example.entities;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is a POJO representing a User
 */
public class User
{
    private final String FIRSTNAME = "firstname";
    private final String LASTNAME  = "lastname";
    private final String NICKNAME  = "nickname";
    private final String EMAIL     = "email";
    private final String PASSWORD  = "password";
    private final String COUNTRY   = "country";


    private final String firstname;
    private final String lastname;
    private final String nickname;
    private final String email;
    private final String password;
    private final String country;

    public User(String firstname, String lastname, String nickname,
                String email, String password, String country)
    {
        this.firstname = firstname.toLowerCase();
        this.lastname = lastname.toLowerCase();
        this.nickname = nickname.toLowerCase();
        this.email = email;
        this.password = password;
        this.country = country.toLowerCase();
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCountry() {
        return country;
    }


    public Map<String, Object> toMap()
    {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(FIRSTNAME, firstname);
        userMap.put(LASTNAME, lastname);
        userMap.put(NICKNAME, nickname);
        userMap.put(EMAIL, email);
        userMap.put(PASSWORD, password);
        userMap.put(COUNTRY, country);

        return userMap;
    }

    @Override
    public boolean equals(Object user)
    {
        boolean result;
        if((user == null) || (getClass() != user.getClass()))
        {
            result = false;
        }
        else
        {
            User otherUser = (User) user;
            result = firstname.equals(otherUser.firstname) &&  lastname.equals(otherUser.lastname) &&
                    nickname.equals(otherUser.nickname) && email.equals(otherUser.email) &&
                    country.equals(otherUser.country);
        }
        return result;
    }
}
