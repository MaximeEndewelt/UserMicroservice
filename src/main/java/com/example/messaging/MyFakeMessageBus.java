package com.example.messaging;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * This class is mocking a message bus, it is a simple
 * unlimited blocking queue
 */
public class MyFakeMessageBus
{
    private static MyFakeMessageBus INSTANCE = null;
    private final BlockingQueue<BusMessage> messageBus;

    private MyFakeMessageBus()
    {
        messageBus = new LinkedBlockingDeque<>();
    }

    public static MyFakeMessageBus getInstance()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new MyFakeMessageBus();
        }

        return INSTANCE;
    }

    public void send(BusMessage message)
    {

    }
}
