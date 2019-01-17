package com.example.entities;

import java.util.HashMap;
import java.util.Map;

/**
 * This enumeration is used to list the types of search criteria
 */
public enum Criteria
{
    FIRSTNAME("firstname"),
    LASTNAME("lastname"),
    NICKNAME("nickname"),
    COUNTRY("country"),
    UNDEFINED("");

    private final String criteriaName;
    private static final Map<String, Criteria > criteriaMap = new HashMap<>();

    static
    {
        for( Criteria criteria : Criteria.values() )
        {
            criteriaMap.put( criteria.criteriaName, criteria );
        }
    }

    private Criteria(String criteriaName)
    {
        this.criteriaName = criteriaName;
    }

    public static Criteria fromString(String criteria)
    {
        Criteria type = criteriaMap.get(criteria);
        if( type == null )
        {
            type = Criteria.UNDEFINED;
        }
        return type;
    }


}
