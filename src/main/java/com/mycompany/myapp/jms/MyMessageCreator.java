package com.mycompany.myapp.jms;

import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public class MyMessageCreator implements MessageCreator {

    @Override
    public Message createMessage(Session session) throws JMSException {
        return session.createMapMessage();
    }

}
