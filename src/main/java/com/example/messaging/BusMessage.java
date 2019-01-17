package com.example.messaging;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents an event used on a message bus
 * Used when an action occurred and we need to notify other services
 */
public class BusMessage
{
    public static final String USER_ADD    = "user_add";
    public static final String USER_UPDATE = "user_update";
    public static final String USER_DELETE = "user_delete";


    private final Map<String, Object> message;

    public BusMessage(String topic, Object messagePayload)
    {
        message = new HashMap<>();
        message.put(topic, messagePayload);
    }

}
